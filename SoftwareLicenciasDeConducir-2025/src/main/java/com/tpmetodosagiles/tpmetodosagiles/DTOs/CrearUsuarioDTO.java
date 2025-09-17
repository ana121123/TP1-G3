package com.tpmetodosagiles.tpmetodosagiles.DTOs;


import lombok.Data;

@Data
public class CrearUsuarioDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String password;
}
