package com.tpmetodosagiles.tpmetodosagiles.DTOs;


import lombok.Data;

@Data
public class ModificarUsuarioDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String usernameSinModificar;
}
