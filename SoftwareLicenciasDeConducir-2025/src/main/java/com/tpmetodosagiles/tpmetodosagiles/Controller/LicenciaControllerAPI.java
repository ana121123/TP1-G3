package com.tpmetodosagiles.tpmetodosagiles.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.*;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;
import com.tpmetodosagiles.tpmetodosagiles.Service.LicenciaService;
import com.tpmetodosagiles.tpmetodosagiles.model.*;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/licencia")
public class LicenciaControllerAPI {
    // Inyectar el servicio TitularService y ServiceAPI para poder utilizar sus
    // métodos en el controlador
    @Autowired
    private LicenciaService licenciaService;
    @Autowired
    private TitularControllerAPI titularControllerAPI;

    @PostMapping("/emitir")
    public ResponseEntity<TitularResponseDTO> crearLicencia(@Valid @RequestBody LicenciaRequestDTO licenciaRequestDTO) {
        Titular titular = titularControllerAPI.obtenerTitularPorDocumento(licenciaRequestDTO.getTipoDocumento(),
                licenciaRequestDTO.getNumeroDocumento());

        // si titular es null devolver un bad_request o un mensaje de que algo salió mal
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con el tipo y número de documento proporcionados.");
        }

        if (licenciaService.tieneEstaSubclaseDeLicencia(titular, licenciaRequestDTO.getClase().getSubcategoria())) {
            throw new MapIllegalArgumentException("subcategoria",
                    "El titular ya tiene una licencia de este tipo");
        }

        Licencia licencia = licenciaService.registrarLicencia(licenciaRequestDTO, titular, false);
        TitularResponseDTO titularResponseDTO = titularControllerAPI.guardarLicenciaNueva(titular, licencia, false);

        // crea y agrega el comprobante al titularResponseDTO
        ComprobanteDTO comprobanteDTO = licenciaService.crearComprobante(licencia, titular);
        titularResponseDTO.setComprobanteDTO(comprobanteDTO);

        return new ResponseEntity<TitularResponseDTO>(titularResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/vigentes")
    public ResponseEntity<List<LicenciaVigenteDTO>> listarLicenciasVigentes(
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String numeroDocumento,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String grupoSanguineo,
            @RequestParam(required = false) String factorRH,
            @RequestParam(required = false) String donante) {

        // Armar objeto FiltrosLicVigentesDTO con los parámetros
        FiltrosLicVigentesDTO filtros = new FiltrosLicVigentesDTO(tipoDocumento, numeroDocumento, nombre, apellido,
                grupoSanguineo, factorRH, donante);
        System.out.println("Filtros recibidos: " + filtros);
        List<LicenciaVigenteDTO> licencias = titularControllerAPI.buscarLicenciasVigentesPorFiltros(filtros);

        if (licencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(licencias, HttpStatus.OK);
    }

    @GetMapping("/vencidas")
    public ResponseEntity<List<LicenciaVencidaDTO>> listarLicenciasVencida(
            @RequestParam(required = false) String tipoDocumento,
            @RequestParam(required = false) String numeroDocumento,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido) {
        // Armar objeto FiltrosLicVencidaDTO con los parámetros
        FiltrosLicVencidasDTO filtros = new FiltrosLicVencidasDTO(tipoDocumento, numeroDocumento, nombre, apellido);

        List<LicenciaVencidaDTO> licencias = titularControllerAPI.buscarLicenciasVencidasPorFiltros(filtros);

        if (licencias.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(licencias, HttpStatus.OK);
    }

    @PostMapping("/renovar")
    public ResponseEntity<TitularResponseDTO> renovarLicencia(
            @RequestParam(required = true) String tipoDocumento,
            @RequestParam(required = true) String numeroDocumento,
            @RequestParam(required = true) String claseLicencia) {
        RenovacionLicenciaRequestDTO renovacionLicenciaRequestDTO = new RenovacionLicenciaRequestDTO(tipoDocumento,
                numeroDocumento, claseLicencia);

        System.out.println("Renovación de licencia solicitada: " + renovacionLicenciaRequestDTO);

        Titular titular = titularControllerAPI.obtenerTitularPorDocumento(tipoDocumento, numeroDocumento);

        // si titular es null devolver un bad_request o un mensaje de que algo salió mal
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con el tipo y número de documento proporcionados.");
        }

        Licencia licencia = licenciaService.renovarLicencia(renovacionLicenciaRequestDTO, titular);
        TitularResponseDTO titularResponseDTO = titularControllerAPI.guardarLicenciaNueva(titular, licencia, true);

        // crea y agrega el comprobante al titularResponseDTO
        ComprobanteDTO comprobanteDTO = licenciaService.crearComprobante(licencia, titular);
        titularResponseDTO.setComprobanteDTO(comprobanteDTO);

        return new ResponseEntity<TitularResponseDTO>(titularResponseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/copia")
    public ResponseEntity<TitularResponseDTO> emitirCopiaLicencia(
            @RequestParam(required = true) String tipoDocumento,
            @RequestParam(required = true) String numeroDocumento,
            @RequestParam(required = true) String claseLicencia) {
        RenovacionLicenciaRequestDTO renovacionLicenciaRequestDTO = new RenovacionLicenciaRequestDTO(tipoDocumento,
                numeroDocumento, claseLicencia);

        System.out.println("Renovación de licencia solicitada: " + renovacionLicenciaRequestDTO);

        Titular titular = titularControllerAPI.obtenerTitularPorDocumento(tipoDocumento, numeroDocumento);

        // si titular es null devolver un bad_request o un mensaje de que algo salió mal
        if (titular == null) {
            throw new MapIllegalArgumentException("documento",
                    "Titular no encontrado con el tipo y número de documento proporcionados.");
        }
        TitularResponseDTO titularResponseDTO = titularControllerAPI.obtenerCopiaTitularResponseDTO(titular);

        // crea y agrega el comprobante al titularResponseDTO
        ComprobanteDTO comprobanteDTO = licenciaService.crearComprobanteEmitirCopia(titular,
                renovacionLicenciaRequestDTO.subClaseLicencia);
        titularResponseDTO.setComprobanteDTO(comprobanteDTO);

        return new ResponseEntity<TitularResponseDTO>(titularResponseDTO, HttpStatus.CREATED);
    }

}
