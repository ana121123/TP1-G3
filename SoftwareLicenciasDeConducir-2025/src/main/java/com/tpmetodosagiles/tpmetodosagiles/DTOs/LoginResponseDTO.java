package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

@Data
public class LoginResponseDTO {
    
    public LoginResponseDTO(String token, String id, String username, String rol) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.rol = rol;
    }

    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String rol;
}
