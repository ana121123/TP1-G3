package com.tpmetodosagiles.tpmetodosagiles.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tpmetodosagiles.tpmetodosagiles.Service.*;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.*;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {
    @Autowired
    private ClaseService claseService;

    // Método para obtener todas las clases de licencia
    @GetMapping("/listar")
    public ResponseEntity<List<ClaseResponseDTO>> listarClases() {
        List<ClaseResponseDTO> clases = claseService.buscarClases();
        return ResponseEntity.ok(clases);
    }

    @GetMapping("/listarCategorias")
    public ResponseEntity<List<String>> listarCategorias() {
        List<String> cat = claseService.buscarCategorias();
        return ResponseEntity.ok(cat);
    }

    @GetMapping("/listarSubcategorias")
    public ResponseEntity<List<String>> listarSubcategorias() {
        List<String> subcat = claseService.buscarSubcategorias();
        return ResponseEntity.ok(subcat);
    }
}
