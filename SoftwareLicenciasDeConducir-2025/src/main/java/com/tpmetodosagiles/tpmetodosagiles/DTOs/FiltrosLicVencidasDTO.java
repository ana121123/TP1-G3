package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

@Data
public class FiltrosLicVencidasDTO {
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombreTitular;
    private String apellidoTitular;

    public FiltrosLicVencidasDTO(String tipoDocumento, String numeroDocumento, String nombreTitular,
            String apellidoTitular) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreTitular = nombreTitular;
        this.apellidoTitular = apellidoTitular;
    }

}
