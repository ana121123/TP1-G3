package com.tpmetodosagiles.tpmetodosagiles.enums;

public enum Rol {
    ADMIN,
    SUPERUSUARIO;

    public static Rol strintToRol(String rol){
        switch (rol) {
            case "ADMIN":
                return Rol.ADMIN;
            case "SUPERUSUARIO":
                return Rol.SUPERUSUARIO;
            default:
                return null;
        }
    }
}
