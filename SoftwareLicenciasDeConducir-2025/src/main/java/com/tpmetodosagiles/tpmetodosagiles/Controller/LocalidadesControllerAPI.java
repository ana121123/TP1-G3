package com.tpmetodosagiles.tpmetodosagiles.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tpmetodosagiles.tpmetodosagiles.Service.*;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.*;

@RestController
@RequestMapping("/api/localidades")
public class LocalidadesControllerAPI {

    @Autowired
    private LocalidadService localidadService;

    // Método para obtener todas las localidades
    @GetMapping("/listar")
    public ResponseEntity<List<LocalidadDTO>> listarLocalidades() {
        List<LocalidadDTO> localidades = localidadService.obtenerTodasLasLocalidadesDTO();
        return ResponseEntity.ok(localidades);
    }

    // Método para buscar una localidad por su nombre
    @GetMapping("/buscar")
    public ResponseEntity<LocalidadDTO> buscarLocalidadPorNombre(@RequestParam String nombre) {
        LocalidadDTO localidad = localidadService.buscarLocalidadPorNombreDTO(nombre);
        if (localidad != null) {
            return ResponseEntity.ok(localidad);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
