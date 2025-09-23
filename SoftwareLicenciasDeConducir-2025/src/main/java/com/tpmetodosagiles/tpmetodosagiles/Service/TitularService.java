package com.tpmetodosagiles.tpmetodosagiles.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tpmetodosagiles.tpmetodosagiles.Repository.*;
import com.tpmetodosagiles.tpmetodosagiles.model.*;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.*;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
// Clase de servicio para manejar la lógica de negocio relacionada con los
// titulares
public class TitularService {
    @Autowired
    private final LocalidadService serviceLocalidad;
    @Autowired
    private final TitularRepository titularRepository;
    @Autowired
    private LicenciaService licenciaService;

    // Método para registr un nuevo titular y validar no existe otro titular con el
    // mismo tipo y número de documento
    public TitularResponseDTO registerTitular(TitularRequestDTO titularDTO) {
        // Verificar si ya existe un titular con el mismo tipo y número de documento
        Titular existingTitular = titularRepository.findByTipoYNumeroDocumento(
                titularDTO.getTipoDocumento(),
                titularDTO.getNumeroDocumento());

        if (existingTitular != null) {
            throw new MapIllegalArgumentException("documento",
                    "Ya existe un titular con el mismo tipo y número de documento.");
        }

        // Mapear DTO a entidad Titular y guardar en la base de datos
        Titular savedTitular = saveTitular(titularDTO);
        return mapToResponseDTO(savedTitular);
    }

    // Método para buscar un titular por nombre y apellido
    public Titular buscarTitularPorNombreYApellido(String nombre, String apellido) {
        Titular titular = titularRepository.findByNombreYApellido(nombre, apellido);
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con nombre: " + nombre + " y apellido: " + apellido);
        }
        return titular;
    }

    // Método para mapear un TitularRequestDTO a un nuevo titular
    private Titular mapToTitular(TitularRequestDTO titularDTO) {
        // Validar si existe un titular con el mismo tipo y número de documento, en tal
        // caso mapearlo
        Titular existingTitular = titularRepository.findByTipoYNumeroDocumento(
                titularDTO.getTipoDocumento(),
                titularDTO.getNumeroDocumento());

        if (existingTitular != null) {
            return existingTitular;
        }

        Titular titular = new Titular();
        titular.setNombre(titularDTO.getNombre());
        titular.setApellido(titularDTO.getApellido());
        titular.setTipoDocumento(titularDTO.getTipoDocumento());
        titular.setNumeroDocumento(titularDTO.getNumeroDocumento());
        titular.setFechaNacimiento(titularDTO.getFechaNacimiento());
        titular.setDireccion(titularDTO.getDireccion());
        titular.setGrupoSanguineo(titularDTO.getGrupoSanguineo());
        titular.setFactorRH(titularDTO.getFactorRH());
        titular.setDonante(titularDTO.getDonante());
        titular.setCuit(titularDTO.getCuit());
        titular.setObservaciones(titularDTO.getObservaciones());
        titular.setLocalidad(serviceLocalidad.buscarLocalidadPorNombre(titularDTO.getLocalidad()));

        return titular;
    }

    // Método para guardar un nuevo titular en la base de datos
    private Titular saveTitular(TitularRequestDTO titularDTO) {
        Titular titular = mapToTitular(titularDTO);
        return titularRepository.save(titular);
    }

    // Método para mapear un Titular a un TitularResponseDTO
    public TitularResponseDTO mapToResponseDTO(Titular titular) {

        TitularResponseDTO responseDTO = new TitularResponseDTO();
        responseDTO.setNombre(titular.getNombre());
        responseDTO.setApellido(titular.getApellido());
        responseDTO.setTipoDocumento(titular.getTipoDocumento());
        responseDTO.setNumeroDocumento(titular.getNumeroDocumento());
        responseDTO.setFechaNacimiento(titular.getFechaNacimiento());
        responseDTO.setDireccion(titular.getDireccion());
        responseDTO.setGrupoSanguineo(titular.getGrupoSanguineo());
        responseDTO.setFactorRH(titular.getFactorRH());
        responseDTO.setDonante(titular.getDonante());
        responseDTO.setCuit(titular.getCuit());
        responseDTO.setObservaciones(titular.getObservaciones());
        responseDTO.setLicenciasVigentes(null);
        responseDTO.setHistorialLicencias(null);
        responseDTO.setLocalidad(titular.getLocalidad().getName());

        // Mapear las licencias vigentes y el historial de licencias si es necesario. Se
        // cargan licenciaResponseDTOs
        // para cada licencia del titular.
        if (titular.getLicenciasVigentes() != null) {
            List<LicenciaResponseDTO> licenciasVigentes = titular.getLicenciasVigentes().stream()
                    .map(licenciaService::mapToResponseDTO)
                    .toList();
            responseDTO.setLicenciasVigentes(licenciasVigentes);
        }

        if (titular.getHistorialLicencias() != null) {
            List<LicenciaResponseDTO> historialLicencias = titular.getHistorialLicencias().stream()
                    .map(licenciaService::mapToResponseDTO)
                    .toList();
            responseDTO.setHistorialLicencias(historialLicencias);
        }

        return responseDTO;
    }

    // Método para encontrar un Titular con el tipo y número de documento
    public Titular buscarTitularPorDNI(String tipoDocumento, String numeroDocumento) {
        Titular titular = titularRepository.findByTipoYNumeroDocumento(tipoDocumento, numeroDocumento);
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con DNI: " + tipoDocumento + " " + numeroDocumento);
        }
        return titular;
    }

    // Método para actualizar la lista de licencias de un titular
    public TitularResponseDTO actualizarTitular(Titular titular, Licencia licencia, boolean esRenovacion) {
        // Actualizar la lista de licencias del titular
        titular.agregarLicenciaVigente(licencia, esRenovacion);
        titularRepository.save(titular);

        return mapToResponseDTO(titular);
    }

    // Método para obtener todos los titulares
    public List<TitularResponseDTO> obtenerTodos() {
        List<Titular> titulares = titularRepository.findAllTitulares();
        if (titulares == null || titulares.isEmpty()) {
            return new ArrayList<>();
        }
        return titulares.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener un titular por tipo de documento y número de documento
    public TitularResponseDTO obtenerTitularPorDocumento(String tipoDocumento, String numeroDocumento) {
        Titular titular = titularRepository.findByTipoYNumeroDocumento(tipoDocumento, numeroDocumento);
        if (titular == null) {
            return null; // O lanzar una excepción si se prefiere
        }
        return mapToResponseDTO(titular);
    }

    public Titular obtenerTitularPorDocumento_Auxiliar(String tipoDocumento, String numeroDocumento) {
        Titular titular = titularRepository.findByTipoYNumeroDocumento(tipoDocumento, numeroDocumento);
        if (titular == null) {
            return null; // O lanzar una excepción si se prefiere
        }
        return titular;
    }

    public List<LicenciaVigenteDTO> obtenerLicenciasVigentes() {
        List<LicenciaVigenteDTO> licenciasVigentes = new ArrayList<>();
        List<Titular> titulares = titularRepository.findAll();

        for (Titular t : titulares) {
            if (t.getLicenciasVigentes() != null) {
                licenciasVigentes.addAll(licenciaService.mapToLicenciaVigenteDTO(t));
            }
        }
        return licenciasVigentes;
    }

    public List<LicenciaVigenteDTO> buscarLicenciasVigentesPorFiltros(FiltrosLicVigentesDTO filtrosDTO) {
        return obtenerLicenciasVigentes().stream()
                .filter(licencia -> aplicarFiltros(licencia, filtrosDTO))
                .collect(Collectors.toList());
    }

    private boolean aplicarFiltros(LicenciaVigenteDTO licencia, FiltrosLicVigentesDTO filtrosDTO) {
        boolean matches = true;

        if (filtrosDTO.getTipoDocumento() != null && !filtrosDTO.getTipoDocumento().isEmpty()) {
            matches &= licencia.getTipoDocumento().equalsIgnoreCase(filtrosDTO.getTipoDocumento());
        }
        if (filtrosDTO.getNumeroDocumento() != null && !filtrosDTO.getNumeroDocumento().isEmpty()) {
            matches &= licencia.getNumeroDocumento().toLowerCase()
                    .contains(filtrosDTO.getNumeroDocumento().toLowerCase());
        }
        if (filtrosDTO.getNombreTitular() != null && !filtrosDTO.getNombreTitular().isEmpty()) {
            matches &= licencia.getNombreTitular().toLowerCase().contains(filtrosDTO.getNombreTitular().toLowerCase());
        }
        if (filtrosDTO.getApellidoTitular() != null && !filtrosDTO.getApellidoTitular().isEmpty()) {
            matches &= licencia.getApellidoTitular().toLowerCase()
                    .contains(filtrosDTO.getApellidoTitular().toLowerCase());
        }
        if (filtrosDTO.getGrupoSanguineoTitular() != null && !filtrosDTO.getGrupoSanguineoTitular().isEmpty()) {
            matches &= licencia.getGrupoSanguineoTitular().equalsIgnoreCase(filtrosDTO.getGrupoSanguineoTitular());
        }
        if (filtrosDTO.getFactorRHTitular() != null && !filtrosDTO.getFactorRHTitular().isEmpty()) {
            matches &= licencia.getFactorRHTitular().equalsIgnoreCase(filtrosDTO.getFactorRHTitular());
        }
        if (filtrosDTO.getDonanteTitular() != null && !filtrosDTO.getDonanteTitular().isEmpty()) {
            matches &= (licencia.isDonanteTitular() == filtrosDTO.esDonante());
        }

        return matches;
    }

    public List<LicenciaVencidaDTO> buscarLicenciasVencidasPorFiltros(FiltrosLicVencidasDTO filtros) {
        return obtenerLicenciasVencidas().stream()
                .filter(licencia -> aplicarFiltrosVencidos(licencia, filtros))
                .collect(Collectors.toList());
    }

    private boolean aplicarFiltrosVencidos(LicenciaVencidaDTO licencia, FiltrosLicVencidasDTO filtros) {
        boolean matches = true;

        if (filtros.getTipoDocumento() != null && !filtros.getTipoDocumento().isEmpty()) {
            matches &= licencia.getTipoDocumento().equalsIgnoreCase(filtros.getTipoDocumento());
        }
        if (filtros.getNumeroDocumento() != null && !filtros.getNumeroDocumento().isEmpty()) {
            matches &= licencia.getNumeroDocumento().toLowerCase()
                    .contains(filtros.getNumeroDocumento().toLowerCase());
        }
        if (filtros.getNombreTitular() != null && !filtros.getNombreTitular().isEmpty()) {
            matches &= licencia.getNombreTitular().toLowerCase()
                    .contains(filtros.getNombreTitular().toLowerCase());
        }
        if (filtros.getApellidoTitular() != null && !filtros.getApellidoTitular().isEmpty()) {
            matches &= licencia.getApellidoTitular().toLowerCase()
                    .contains(filtros.getApellidoTitular().toLowerCase());
        }

        return matches;
    }

    private List<LicenciaVencidaDTO> obtenerLicenciasVencidas() {
        List<LicenciaVencidaDTO> licenciasVencidas = new ArrayList<>();
        List<Titular> titulares = titularRepository.findAll();

        for (Titular t : titulares) {
            if (t.getHistorialLicencias() != null) {
                licenciasVencidas.addAll(licenciaService.mapToLicenciaVencidaDTO(t));
            }
        }
        return licenciasVencidas;
    }

    public TitularResponseDTO modificarTitular(TitularRequestDTO titularRequestDTO) {
        Titular titular = titularRepository.findByTipoYNumeroDocumento(titularRequestDTO.getTipoDocumento(),
                titularRequestDTO.getNumeroDocumento());
        Localidad localidad = serviceLocalidad.buscarLocalidadPorNombre(titularRequestDTO.getLocalidad());
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con el tipo y número de documento proporcionados.");
        }
        if (localidad == null) {
            throw new MapIllegalArgumentException("localidad", "Localidad no encontrada.");
        }

        // Mapear los campos del DTO al objeto Titular
        titular.setNombre(titularRequestDTO.getNombre());
        titular.setApellido(titularRequestDTO.getApellido());
        titular.setLocalidad(localidad);
        titular.setTipoDocumento(titularRequestDTO.getTipoDocumento());
        titular.setDireccion(titularRequestDTO.getDireccion());
        titular.setGrupoSanguineo(titularRequestDTO.getGrupoSanguineo());
        titular.setFactorRH(titularRequestDTO.getFactorRH());
        titular.setDonante(titularRequestDTO.getDonante());
        titular.setObservaciones(titularRequestDTO.getObservaciones());
        titular.setFechaNacimiento(titularRequestDTO.getFechaNacimiento());

        for (Licencia licencia : titular.getLicenciasVigentes()) {
            licencia.setCosto(50);
        }

        titularRepository.save(titular);
        TitularResponseDTO titularResponseDTO = mapToResponseDTO(titular);
        generarComprobanteDTO(titularResponseDTO);
        titularResponseDTO.getComprobanteDTO().setEdadTitular(titular.calcularEdad());
        return titularResponseDTO;
    }

    private void generarComprobanteDTO(TitularResponseDTO t) {
        ComprobanteDTO comprobanteDTO = new ComprobanteDTO();
        comprobanteDTO.setClaseLicencia(t.getLicenciasVigentes().get(0).getClaseLicencia().getCategoria().toString());
        comprobanteDTO.setCosto("50");
        comprobanteDTO.setNombreUsuarioEmisor("Super");
        comprobanteDTO.setApellidoUsuarioEmisor("Usuario");
        comprobanteDTO.setNombreTitular(t.getNombre());
        comprobanteDTO.setApellidoTitular(t.getApellido());
        comprobanteDTO.setTipoDocumento(t.getTipoDocumento());
        comprobanteDTO.setNumeroDocumento(t.getNumeroDocumento());
        comprobanteDTO.setFechahoraEmisión(java.time.LocalDateTime.now());

        t.setComprobanteDTO(comprobanteDTO);

    }

    private List<Titular> filtrarPorRH (String rh){
        List<Titular> titulares = titularRepository.findAll();

        List<Titular> titularesRh = titulares.stream()
            .filter(t -> t.getFactorRH().equalsIgnoreCase(rh))
            .collect(Collectors.toList());

        return titularesRh;
    }

    private List<Titular> filtrarPorNombre (String nombre){
        List<Titular> titulares = titularRepository.findAll();

        List<Titular> titularesNombre = titulares.stream()
            .filter(t -> t.getNombre().equalsIgnoreCase(nombre))
            .collect(Collectors.toList());

        return titularesNombre;
    }
}