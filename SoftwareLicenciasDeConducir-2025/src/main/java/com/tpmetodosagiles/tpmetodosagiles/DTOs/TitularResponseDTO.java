package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;

import java.time.LocalDate;

@Data
public class TitularResponseDTO {
    private String nombre;
    private String apellido;
    private String tipoDocumento; // DNI, Pasaporte, etc.
    private String numeroDocumento; // Número de documento
    private LocalDate fechaNacimiento;
    private String direccion;
    private String grupoSanguineo; // A, B, AB, O
    private String factorRH; // Positivo o Negativo
    private boolean donante; // Condición de donante
    private String cuit;
    private String observaciones;

    private List<LicenciaResponseDTO> licenciasVigentes;
    private List<LicenciaResponseDTO> historialLicencias;
    private ComprobanteDTO comprobanteDTO;
    private String localidad;

    // Set y get para las listas de licencias
    public List<LicenciaResponseDTO> getLicenciasVigentes() {
        return licenciasVigentes;
    }

    public void setLicenciasVigentes(List<LicenciaResponseDTO> licenciasVigentes) {
        this.licenciasVigentes = licenciasVigentes;
    }

    public List<LicenciaResponseDTO> getHistorialLicencias() {
        return historialLicencias;
    }

    public void setHistorialLicencias(List<LicenciaResponseDTO> historialLicencias) {
        this.historialLicencias = historialLicencias;
    }

}
