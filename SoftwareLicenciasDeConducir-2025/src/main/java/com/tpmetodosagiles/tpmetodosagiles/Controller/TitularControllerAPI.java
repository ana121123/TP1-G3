package com.tpmetodosagiles.tpmetodosagiles.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.FiltrosLicVencidasDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.FiltrosLicVigentesDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaVencidaDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LicenciaVigenteDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;
import com.tpmetodosagiles.tpmetodosagiles.Service.LocalidadService;
import com.tpmetodosagiles.tpmetodosagiles.Service.TitularService;
import com.tpmetodosagiles.tpmetodosagiles.model.Licencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Titular;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/titular")
public class TitularControllerAPI {
    // Inyectar el servicio TitularService y ServiceAPI para poder utilizar sus
    // métodos en el controlador
    @Autowired
    private TitularService titularService;

    // método crearTitular() que reciba un TitularRequestDTO, llame al servicio y
    // devuelva un TitularResponseDTO
    @PostMapping("/crear")
    public ResponseEntity<?> crearTitular(@Valid @RequestBody TitularRequestDTO titularRequestDTO) {
        // Validar el DTO recibido
        // retornar un objeto error si el DTO es inválido

        // validar que la localidad esté en la base
        LocalidadService localidadService = titularService.getServiceLocalidad();
        if (localidadService.buscarLocalidadPorNombre(titularRequestDTO.getLocalidad()) == null) {
            throw new MapIllegalArgumentException("localidad",
                    "localidad no encontrada.");
        }

        TitularResponseDTO titularResponseDTO = titularService.registerTitular(titularRequestDTO);
        return ResponseEntity.ok(titularResponseDTO);
    }

    // Método para obtener un titular por tipo de documento y número de documento
    @GetMapping("/obtener")
    public ResponseEntity<?> obtenerTitular(@RequestParam String tipoDocumento, @RequestParam String numeroDocumento) {
        TitularResponseDTO titularResponseDTO = titularService.obtenerTitularPorDocumento(tipoDocumento,
                numeroDocumento);
        if (titularResponseDTO != null) {
            return ResponseEntity.ok(titularResponseDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Método para buscar un titular por tipo y número de documento
    public Titular obtenerTitularPorDocumento(String tipoDocumento, String numeroDocumento) {
        return titularService.obtenerTitularPorDocumento_Auxiliar(tipoDocumento, numeroDocumento);
    }

    public TitularResponseDTO guardarLicenciaNueva(Titular titular, Licencia licencia, boolean esRenovacion) {
        return titularService.actualizarTitular(titular, licencia, esRenovacion);
    }

    // Método para listar todos los titulares
    @GetMapping("/listar")
    public List<TitularResponseDTO> getAllTitulares() {

        return titularService.obtenerTodos();
    }

    @PostMapping("/modificar")
    public ResponseEntity<?> modificarTitular(@Valid @RequestBody TitularRequestDTO titularRequestDTO) {

        LocalidadService localidadService = titularService.getServiceLocalidad();
        if (localidadService.buscarLocalidadPorNombre(titularRequestDTO.getLocalidad()) == null) {
            throw new MapIllegalArgumentException("localidad",
                    "localidad no encontrada.");
        }

        TitularResponseDTO titularResponseDTO = titularService.modificarTitular(titularRequestDTO);
        return ResponseEntity.ok(titularResponseDTO);
    }

    public List<LicenciaVigenteDTO> obtenerLicenciasVigentes() {
        List<LicenciaVigenteDTO> licenciasVigentes = new ArrayList<>();
        licenciasVigentes = titularService.obtenerLicenciasVigentes();

        return licenciasVigentes;
    }

    public List<LicenciaVigenteDTO> buscarLicenciasVigentesPorFiltros(FiltrosLicVigentesDTO filtrosDTO) {
        return titularService.buscarLicenciasVigentesPorFiltros(filtrosDTO);
    }

    public TitularResponseDTO obtenerCopiaTitularResponseDTO(Titular titular) {
        return titularService.mapToResponseDTO(titular);
    }

    public List<LicenciaVencidaDTO> buscarLicenciasVencidasPorFiltros(FiltrosLicVencidasDTO filtros) {
        return titularService.buscarLicenciasVencidasPorFiltros(filtros);
    }

}
