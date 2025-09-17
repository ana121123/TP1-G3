package com.tpmetodosagiles.tpmetodosagiles.enums;

public enum ClaseLicencia {
    A,
    B,
    C,
    D,
    E,
    F,
    G;

    // Metodo que traduce el enum a un String
    public String getNombre() {
        return this.name();
    }

    // Metodo para traducir el String a un enum
    public static ClaseLicencia fromString(String nombre) {
        for (ClaseLicencia clase : ClaseLicencia.values()) {
            if (clase.name().equalsIgnoreCase(nombre)) {
                return clase;
            }
        }
        throw new IllegalArgumentException("Clase de licencia no válida: " + nombre);
    }

    // Método para devolver la edad mínima requerida para cada clase de licencia
    public int getEdadMinima() {
        switch (this) {
            case A:
                return 17; // Licencia A
            case B:
                return 17; // Licencia B
            case C:
                return 21; // Licencia C
            case D:
                return 21; // Licencia D
            case E:
                return 21; // Licencia E
            case F:
                return 17; // Licencia F
            case G:
                return 17; // Licencia G
            default:
                throw new IllegalArgumentException("Clase de licencia no válida: " + this.name());
        }
    }

    // Método que valida si la clase es o no profesional
    public boolean esProfesional() {
        return this == C || this == D || this == E;
    }

}
