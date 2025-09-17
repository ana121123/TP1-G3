package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import java.time.LocalDate;

import lombok.Data;

@Data

public class LicenciaVencidaDTO {
    private String tipoDocumento;
    private String numeroDocumento;

    private String claseLicencia;
    private LocalDate fechaExpiracion;
    private String nombreTitular;
    private String apellidoTitular;
    private String grupoSanguineoTitular;
    private String factorRHTitular;
    private boolean donanteTitular;
    private boolean puedeRenovar;
}
