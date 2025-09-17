package com.tpmetodosagiles.tpmetodosagiles.Service;

import org.springframework.stereotype.Service;
import com.tpmetodosagiles.tpmetodosagiles.model.*;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.*;
import com.tpmetodosagiles.tpmetodosagiles.Repository.*;
import com.tpmetodosagiles.tpmetodosagiles.enums.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {
    @Autowired
    private ClaseRepository claseRepository;

    // metodo para mapear a una clase
    public Clase mapToClase(ClaseRequestDTO claseRequestDTO) {
        return claseRepository.findBySubcategoria(claseRequestDTO.getSubcategoria());
    }

    // Método para mapear una claseResponseDTO
    public ClaseResponseDTO mapToClaseDTO(Clase clase) {
        ClaseResponseDTO claseDTO = new ClaseResponseDTO();
        claseDTO.setCategoria(clase.getCategoria());
        claseDTO.setSubcategoria(clase.getSubcategoria());
        claseDTO.setTitulo(clase.getTitulo());
        claseDTO.setDescripcion(clase.getDescripcion());

        return claseDTO;
    }

    // Método para buscar todas las clases de licencia
    public List<ClaseResponseDTO> buscarClases() {
        List<Clase> clases = claseRepository.findAll();
        return clases.stream()
                .map(this::mapToClaseDTO)
                .collect(Collectors.toList());
    }

    public List<String> buscarSubcategorias() {
        List<Clase> clases = claseRepository.findAll();
        return clases.stream()
                .map(Clase::getSubcategoria)
                .collect(Collectors.toList());
    }

    public List<String> buscarCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add(ClaseLicencia.A.toString());
        categorias.add(ClaseLicencia.B.toString());
        categorias.add(ClaseLicencia.C.toString());
        categorias.add(ClaseLicencia.D.toString());
        categorias.add(ClaseLicencia.E.toString());
        categorias.add(ClaseLicencia.F.toString());
        categorias.add(ClaseLicencia.G.toString());

        return categorias;
    }

}
