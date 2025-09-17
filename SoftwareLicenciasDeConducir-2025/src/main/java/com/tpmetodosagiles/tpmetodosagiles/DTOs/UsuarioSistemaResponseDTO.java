package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

@Data
public class UsuarioSistemaResponseDTO {
    private String username;
    private String nombre;
    private String apellido;

    public UsuarioSistemaResponseDTO(String username, String nombre, String apellido) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;

    }

}
