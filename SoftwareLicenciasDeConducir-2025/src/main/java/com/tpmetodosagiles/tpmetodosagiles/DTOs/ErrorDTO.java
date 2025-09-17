package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private String mensaje;
    private String codigo;
    private ArrayList<String> listaDetalles = new ArrayList<>();
}
