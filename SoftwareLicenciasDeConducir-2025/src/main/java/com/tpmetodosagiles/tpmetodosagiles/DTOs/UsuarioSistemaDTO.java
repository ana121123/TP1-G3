package com.tpmetodosagiles.tpmetodosagiles.DTOs;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioSistemaDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String password;
}
