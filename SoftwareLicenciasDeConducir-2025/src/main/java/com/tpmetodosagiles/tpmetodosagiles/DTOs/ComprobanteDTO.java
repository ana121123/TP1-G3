package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ComprobanteDTO {
    private String claseLicencia;
    private String costo;

    private String nombreUsuarioEmisor;
    private String apellidoUsuarioEmisor;

    private String nombreTitular;
    private String apellidoTitular;
    private String tipoDocumento; // Titular
    private String numeroDocumento;// Titular

    private int edadTitular; // Titular

    private LocalDateTime fechahoraEmisión;
}
