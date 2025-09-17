package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;

@Data
public class FiltrosLicVigentesDTO {
    String tipoDocumento;
    String numeroDocumento;
    String nombreTitular;
    String apellidoTitular;
    String grupoSanguineoTitular;
    String factorRHTitular;
    String donanteTitular;

    public boolean esDonante() {
        if (donanteTitular.equalsIgnoreCase("false")) {
            return false;
        }
        if (donanteTitular.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public FiltrosLicVigentesDTO(String tipoDocumento, String numeroDocumento, String nombreTitular,
            String apellidoTitular, String grupoSanguineoTitular, String factorRHTitular, String donanteTitular) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombreTitular = nombreTitular;
        this.apellidoTitular = apellidoTitular;
        this.grupoSanguineoTitular = grupoSanguineoTitular;
        this.factorRHTitular = factorRHTitular;
        this.donanteTitular = donanteTitular;
    }
}
