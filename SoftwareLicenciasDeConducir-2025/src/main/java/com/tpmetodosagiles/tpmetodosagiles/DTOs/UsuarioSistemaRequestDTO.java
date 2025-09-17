package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

@Data
public class UsuarioSistemaRequestDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellido;
}
