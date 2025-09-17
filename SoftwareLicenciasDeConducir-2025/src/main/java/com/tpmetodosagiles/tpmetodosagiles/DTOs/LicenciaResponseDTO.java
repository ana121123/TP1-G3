package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import java.time.LocalDate;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Clase;

import lombok.Data;

@Data
public class LicenciaResponseDTO {

    private ClaseResponseDTO claseLicencia;
    private String observaciones;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private String estado; // Activa, Inactiva, Expirada
    private int costo;
    private String localidad;
    private UsuarioSistemaResponseDTO usuarioSistema;

}
