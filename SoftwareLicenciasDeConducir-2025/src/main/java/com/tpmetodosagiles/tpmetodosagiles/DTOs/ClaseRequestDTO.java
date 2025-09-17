package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;

import lombok.Data;

@Data
public class ClaseRequestDTO {
    private ClaseLicencia categoria;
    private String subcategoria;
}
