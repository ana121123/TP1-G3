package com.tpmetodosagiles.tpmetodosagiles.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.ComprobanteDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaVencidaDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaVigenteDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.RenovacionLicenciaRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.UsuarioSistemaResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;
import com.tpmetodosagiles.tpmetodosagiles.enums.CostoLicencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Clase;
import com.tpmetodosagiles.tpmetodosagiles.model.Licencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Titular;

import jakarta.validation.Valid;

import com.tpmetodosagiles.tpmetodosagiles.Exceptions.CostoInvalidoException;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Service
@Data
@RequiredArgsConstructor
public class LicenciaService {

    // private final TitularRepository titularRepository;
    @Autowired
    private final LocalidadService localidadService;
    @Autowired
    private final UsuarioSistemaRepository usuarioSistemaRepository;
    @Autowired
    private final ClaseService claseService;

    // Metodo para registrar una nueva licencia
    public Licencia registrarLicencia(LicenciaRequestDTO licenciaDTO, Titular titular, boolean renovacion) {
        // Campos validados en el Controller
        System.out.println("llegué al servicio de registro de licencia");

        // Validar que el titular no sea nulo (SE HACE EN EL CONTROLLER)
        /*
         * if (titular == null) {
         * throw new MapIllegalArgumentException("documento",
         * "Titular no encontrado con el tipo y número de documento proporcionados.");
         * }
         */

        System.out.println("el titular no es nulo");
        // Validar edad del titular
        validarEdadTitular(licenciaDTO, titular);
        System.out.println("la edad del titular es valida");

        // Validar que el titular no tenga una licencia vigente del mismo tipo.
        if (!renovacion) {
            validacionPrimeraVez(licenciaDTO, titular);
            System.out.println("validación de primera vez realizada");
            // Validar que no supere los 65 años si es la primera vez que solicita una
            // licencia profesional
            if (licenciaDTO.getClase().getCategoria().esProfesional() && titular.calcularEdad() > 65) {
                throw new MapIllegalArgumentException("not specific",
                        "El titular no puede registrar una licencia profesional por primera vez si tiene más de 65 años.");
            }
        } else {
            // validar si la licencia falta menos de 15 días para que venza
            if (!licenciaConMenosDe15DiasDeVigencia(licenciaDTO, titular)) {
                validacionPrimeraVez(licenciaDTO, titular);
            }

        }

        // Validar que el titular tengo en su historial una licencia vencida de clase B
        // si la solicitada es una licencia profesional
        if (licenciaDTO.getClase().getCategoria().esProfesional()
                && !tieneLicenciaVencidaClase(titular, ClaseLicencia.B)) {
            throw new MapIllegalArgumentException("not specific",
                    "El titular debe tener una licencia vencida de clase B para registrar una licencia profesional.");
        }
        System.out.println("el titular tiene una licencia vencida de clase B o no es una licencia profesional");

        // Lógica para registrar la nueva licencia
        return guardarLicencia(licenciaDTO, titular, renovacion);
    }

    private void validacionPrimeraVez(LicenciaRequestDTO licenciaDTO, Titular titular) {
        if (titular.PasaronMenosDeNmesesDesdeSuCumpleaños(6) || titular.faltanMeneosDeNmesesParaSuCumpleños(2)) {

        } else {
            throw new MapIllegalArgumentException("not specific",
                    "no es una ventana de tiempo valida para renovar/sacar la licencia");
        }
    }

    // Validar que el titular no tenga una licencia vigente del mismo tipo
    private boolean licenciaConMenosDe15DiasDeVigencia(LicenciaRequestDTO licenciaDTO, Titular titular) {
        // Verificar si el titular ya tiene una licencia vigente del mismo tipo y faltan
        // mas de 15 días para que venza
        boolean tieneLicenciaVigente = titular.getLicenciasVigentes().stream()
                .anyMatch(licencia -> licencia.getClaseString().equals(licenciaDTO.getClaseString())
                        && licencia.calcularDiasParaVencimiento() > 15);
        if (tieneLicenciaVigente) {
            throw new IllegalArgumentException(
                    "El titular ya tiene una licencia vigente del mismo tipo y faltan mas de 15 días para que venza.");
        }
        return false;
    }

    // Validar que la edad ingresada sea la del titular, verificar con la fecha de
    // nacimiento
    private void validarEdadTitular(LicenciaRequestDTO licenciaDTO, Titular titular) {
        // Calcular la edad del titular a partir de su fecha de nacimiento
        int edadTitular = titular.calcularEdad();
        if (edadTitular != licenciaDTO.getEdadTitular()) {
            throw new MapIllegalArgumentException("edad", "La edad no coincide con la del titular.");
        }
        if (edadTitular < licenciaDTO.getClase().getCategoria().getEdadMinima()) {
            throw new MapIllegalArgumentException("edad",
                    "El titular debe ser mayor de " + licenciaDTO.getClase().getCategoria().getEdadMinima()
                            + " años para registrar una licencia.");
        }
    }

    //// Metodo para buscar si hay una licencia venciada clase claseLicencia. Si
    //// está
    //// en el historial de licencias del titular, se considera que tiene una
    //// licencia vencida de esa clase.
    // Si no está en el historial, se considera que no tiene una licencia vencida de
    //// esa clase.
    private boolean tieneLicenciaVencidaClase(Titular titular, ClaseLicencia clase) {
        return titular.getHistorialLicencias().stream()
                .filter(licencia -> licencia.getClaseString().equals(clase.getNombre()))
                .findAny()
                .isPresent();
    }

    // Método para guardar la licencia y actualizar el titular
    private Licencia guardarLicencia(LicenciaRequestDTO licenciaDTO, Titular titular, boolean renovacion) {
        // Mapear el DTO a una entidad Licencia con un metodo privado
        Licencia nuevaLicencia = mapToLicenciaNueva(licenciaDTO);
        nuevaLicencia.setFechaEmision(LocalDate.now());
        nuevaLicencia.setFechaVencimiento(calcularFechaVencimiento(titular, renovacion));
        nuevaLicencia.setCosto(calcularCostoTotal(nuevaLicencia, titular, renovacion, LocalDate.now()));
        return nuevaLicencia;
    }

    // Método para mapear un LicenciaRequestDTO a una entidad Licencia
    public Licencia mapToLicenciaNueva(LicenciaRequestDTO licenciaDTO) {
        Licencia licencia = new Licencia();
        licencia.setObservaciones(licenciaDTO.getObservaciones());
        licencia.setClase(claseService.mapToClase(licenciaDTO.getClase()));
        licencia.setEstado("Vigente"); // Estado inicial de la licencia
        licencia.setLocalidadLicencia(localidadService.buscarLocalidadPorNombre(licenciaDTO.getLocalidad()));
        licencia.setUsuarioSistema(usuarioSistemaRepository.findByUsername(licenciaDTO.getUsername()));

        return licencia;
    }

    // Método para mapear una entidad Licencia a un LicenciaResponseDTO
    public LicenciaResponseDTO mapToResponseDTO(Licencia licencia) {
        LicenciaResponseDTO responseDTO = new LicenciaResponseDTO();

        responseDTO.setClaseLicencia(claseService.mapToClaseDTO(licencia.getClase()));
        responseDTO.setFechaEmision(licencia.getFechaEmision());
        responseDTO.setFechaVencimiento(licencia.getFechaVencimiento());
        responseDTO.setObservaciones(licencia.getObservaciones());
        responseDTO.setEstado(licencia.getEstado());
        responseDTO.setCosto(licencia.getCosto());
        responseDTO.setLocalidad(licencia.getLocalidadLicencia().getName());
        responseDTO.setUsuarioSistema(new UsuarioSistemaResponseDTO(
                licencia.getUsuarioSistema().getUsername(), licencia.getUsuarioSistema().getNombre(),
                licencia.getUsuarioSistema().getApellido()));

        return responseDTO;
    }

    public int calcularCostoTotal(Licencia licencia, Titular titular, boolean renovacion,
            LocalDate fechaParaCalcularEdad) {
        int anios = calcularAniosDeVigencia(titular, fechaParaCalcularEdad, renovacion);
        return CostoLicencia.obtenerCostoTotal(licencia.getClase().getCategoria().getNombre(), anios);
    }

    public LocalDate calcularCumpleanioEmision(Titular titular) {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaNacimiento = titular.getFechaNacimiento();
        LocalDate fechaCumpleanioEmision;

        // Calcular ultimo cumpleaños
        LocalDate fechaUltimoCumpleanios;
        if (fechaNacimiento.getDayOfMonth() == 29 && fechaNacimiento.getDayOfMonth() == 2) {
            fechaUltimoCumpleanios = LocalDate.of(fechaActual.getYear(), 3, 1);
        } else {
            fechaUltimoCumpleanios = LocalDate.of(fechaActual.getYear(), fechaNacimiento.getMonthValue(),
                    fechaNacimiento.getDayOfMonth());
        }
        if (fechaActual.isBefore(fechaUltimoCumpleanios)) {
            fechaUltimoCumpleanios = fechaUltimoCumpleanios.minusYears(1);
        }

        // Ver si está más cerca del ultimo cumpleaños o del siguiente
        int diasHastaUltimoCumpleanios = java.time.Period.between(fechaUltimoCumpleanios, fechaActual).getDays();
        int diasHastaSiguienteCumpleanios = java.time.Period.between(fechaActual, fechaUltimoCumpleanios.plusYears(1))
                .getDays();
        if (diasHastaUltimoCumpleanios <= diasHastaSiguienteCumpleanios)
            fechaCumpleanioEmision = fechaUltimoCumpleanios;
        else
            fechaCumpleanioEmision = fechaUltimoCumpleanios.plusYears(1);

        return fechaCumpleanioEmision;
    }

    public LocalDate calcularFechaVencimiento(Titular titular, boolean renovacion) {
        LocalDate fechaVencimiento = calcularCumpleanioEmision(titular)
                .plusYears(calcularAniosDeVigencia(titular, LocalDate.now(), renovacion));

        return fechaVencimiento;
    }

    public int calcularAniosDeVigencia(Titular titular, LocalDate fechaParaCalcularEdad, boolean renovacion) {
        int edad = titular.calcularEdad(fechaParaCalcularEdad);
        int anios;
        if (edad < 21) {
            if (renovacion)
                anios = 3;
            else
                anios = 1;
        } else {
            if (edad <= 46)
                anios = 5;
            else {
                if (edad <= 60)
                    anios = 4;
                else {
                    if (edad <= 70)
                        anios = 3;
                    else
                        anios = 1;
                }
            }
        }

        return anios;
    }

    public void validarCalculoDeCosto(Licencia licencia, Titular titular, boolean renovacion)
            throws CostoInvalidoException {
        int costoCalculado = calcularCostoTotal(licencia, titular, renovacion, licencia.getFechaEmision());
        if (licencia.getCosto() != costoCalculado) {
            String mensajeException = "El costo en la licencia es " + licencia.getCosto() + " y el verificado da "
                    + costoCalculado;
            throw new CostoInvalidoException(mensajeException);
        }
    }

    public ComprobanteDTO crearComprobante(Licencia licencia, Titular titular) {
        ComprobanteDTO comprobanteDTO = new ComprobanteDTO();
        comprobanteDTO.setClaseLicencia(licencia.getClaseString());
        comprobanteDTO.setCosto(((Integer) licencia.getCosto()).toString());
        comprobanteDTO.setNombreUsuarioEmisor(licencia.getUsuarioSistema().getNombre());
        comprobanteDTO.setApellidoUsuarioEmisor(licencia.getUsuarioSistema().getApellido());
        comprobanteDTO.setNombreTitular(titular.getNombre());
        comprobanteDTO.setApellidoTitular(titular.getApellido());
        comprobanteDTO.setTipoDocumento(titular.getTipoDocumento());
        comprobanteDTO.setNumeroDocumento(titular.getNumeroDocumento());
        comprobanteDTO.setFechahoraEmisión(LocalDateTime.now());
        comprobanteDTO.setEdadTitular(titular.calcularEdad());

        return comprobanteDTO;
    }

    public boolean tieneEstaSubclaseDeLicencia(Titular titular, String claseString) {
        boolean flag = false;

        if (titular.getLicenciasVigentes() != null) {
            for (Licencia lv : titular.getLicenciasVigentes()) {
                if (lv.getClase().getSubcategoria().equals(claseString)) {
                    flag = true;
                    // System.out.println("ENCONTRE UNA LICENCIA");
                    break;
                }
            }
        }
        if (titular.getHistorialLicencias() != null && !flag) {
            for (Licencia hist : titular.getHistorialLicencias()) {
                if (hist.getClase().getSubcategoria().equals(claseString)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public List<LicenciaVigenteDTO> mapToLicenciaVigenteDTO(Titular t) {

        List<LicenciaVigenteDTO> dtos = new ArrayList<>();
        if (t.getLicenciasVigentes() != null) {
            for (Licencia lv : t.getLicenciasVigentes()) {
                LicenciaVigenteDTO dto = new LicenciaVigenteDTO();
                dto.setTipoDocumento(t.getTipoDocumento());
                dto.setNumeroDocumento(t.getNumeroDocumento());
                dto.setApellidoTitular(t.getApellido());
                dto.setNombreTitular(t.getNombre());
                dto.setClaseLicencia(lv.getClase().getSubcategoria());
                dto.setFechaExpiracion(lv.getFechaVencimiento());
                dto.setGrupoSanguineoTitular(t.getGrupoSanguineo());
                dto.setFactorRHTitular(t.getFactorRH());
                dto.setDonanteTitular(t.getDonante());
                // si faltan menos de 15 días para que venza, puede renovar
                dto.setPuedeRenovar(lv.calcularDiasParaVencimiento() <= 15);

                dtos.add(dto);
            }
        }
        return dtos;
    }

    public Licencia renovarLicencia(RenovacionLicenciaRequestDTO renovacionLicenciaRequestDTO, Titular titular) {
        if (tieneEstaSubclaseDeLicenciaVigente(titular, renovacionLicenciaRequestDTO.getSubClaseLicencia())) {
            Licencia nuevaLicencia = buscarLicVigPorSubCategoria(titular,
                    renovacionLicenciaRequestDTO.getSubClaseLicencia());
            // if(licenciaConMenosDe15DiasDeVigencia(nuevaLicencia, titular))
            nuevaLicencia.setFechaEmision(LocalDate.now());
            nuevaLicencia.setFechaVencimiento(calcularFechaVencimiento(titular, true));
            return nuevaLicencia;
        }
        if (tieneEstaSubclaseDeLicenciaVencida(titular, renovacionLicenciaRequestDTO.getSubClaseLicencia())) {
            Licencia nuevaLicencia = buscarLicVenPorSubCategoria(titular,
                    renovacionLicenciaRequestDTO.getSubClaseLicencia());
            nuevaLicencia.setFechaEmision(LocalDate.now());
            nuevaLicencia.setFechaVencimiento(calcularFechaVencimiento(titular, true));
            return nuevaLicencia;
        }
        return null;
    }

    public boolean tieneEstaSubclaseDeLicenciaVigente(Titular titular, String claseString) {
        boolean flag = false;

        if (titular.getLicenciasVigentes() != null) {
            for (Licencia lv : titular.getLicenciasVigentes()) {
                if (lv.getClase().getSubcategoria().equals(claseString)) {
                    flag = true;
                    // System.out.println("ENCONTRE UNA LICENCIA");
                    break;
                }
            }
        }
        return flag;
    }

    public boolean tieneEstaSubclaseDeLicenciaVencida(Titular titular, String claseString) {
        boolean flag = false;
        if (titular.getHistorialLicencias() != null && !flag) {
            for (Licencia hist : titular.getHistorialLicencias()) {
                if (hist.getClase().getSubcategoria().equals(claseString)) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    public Licencia buscarLicVigPorSubCategoria(Titular titular, String subCategoria) {
        if (titular.getLicenciasVigentes() != null) {
            for (Licencia lv : titular.getLicenciasVigentes()) {
                if (lv.getClase().getSubcategoria().equals(subCategoria)) {
                    return lv;
                }
            }
        }
        return null;
    }

    public Licencia buscarLicVenPorSubCategoria(Titular titular, String subCategoria) {
        // buscar la ultima licencia vencida del titular que tenga la subcategoria
        // si no la encuentra, devuelve null
        if (titular.getHistorialLicencias() != null) {
            for (Licencia hist : titular.getHistorialLicencias()) {
                if (hist.getClase().getSubcategoria().equals(subCategoria)) {
                    return hist;
                }
            }
        }
        return null;
    }

    public ComprobanteDTO crearComprobanteEmitirCopia(Titular titular, String subClaseLicencia) {
        ComprobanteDTO comprobanteDTO = new ComprobanteDTO();
        comprobanteDTO.setClaseLicencia(subClaseLicencia);
        comprobanteDTO.setCosto("50"); // Costo de la copia es 50
        comprobanteDTO.setNombreUsuarioEmisor("Super");
        comprobanteDTO.setApellidoUsuarioEmisor("Usuario");
        comprobanteDTO.setNombreTitular(titular.getNombre());
        comprobanteDTO.setApellidoTitular(titular.getApellido());
        comprobanteDTO.setTipoDocumento(titular.getTipoDocumento());
        comprobanteDTO.setNumeroDocumento(titular.getNumeroDocumento());
        comprobanteDTO.setFechahoraEmisión(LocalDateTime.now());
        comprobanteDTO.setEdadTitular(titular.calcularEdad());
        return comprobanteDTO;
    }

    public List<LicenciaVencidaDTO> mapToLicenciaVencidaDTO(Titular t) {
        List<LicenciaVencidaDTO> licenciasVencidasDTO = new ArrayList<>();
        if (t.getHistorialLicencias() != null) {
            for (Licencia licencia : t.getHistorialLicencias()) {
                LicenciaVencidaDTO dto = new LicenciaVencidaDTO();
                dto.setTipoDocumento(t.getTipoDocumento());
                dto.setNumeroDocumento(t.getNumeroDocumento());
                dto.setClaseLicencia(licencia.getClase().getSubcategoria());
                dto.setFechaExpiracion(licencia.getFechaVencimiento());
                dto.setNombreTitular(t.getNombre());
                dto.setApellidoTitular(t.getApellido());
                dto.setGrupoSanguineoTitular(t.getGrupoSanguineo());
                dto.setFactorRHTitular(t.getFactorRH());
                dto.setDonanteTitular(t.getDonante());
                dto.setPuedeRenovar(t.PasaronMenosDeNmesesDesdeSuCumpleaños(6)
                        || t.faltanMeneosDeNmesesParaSuCumpleños(2));
                licenciasVencidasDTO.add(dto);
            }
        }
        return licenciasVencidasDTO;
    }
}