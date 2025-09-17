package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import com.tpmetodosagiles.tpmetodosagiles.enums.*;

import lombok.Data;

@Data
public class ClaseResponseDTO {
    private ClaseLicencia categoria;
    private String subcategoria;
    private String titulo;
    private String descripcion;

}
