package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class RenovacionLicenciaRequestDTO {
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Size(max = 50, message = "El tipoDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El tipoDocumento solo puede contener letras y espacios.")
    public String tipoDocumento;

    @NotBlank(message = "El número de documento es obligatorio")
    @Size(min = 7, max = 8, message = "El numeroDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El numeroDocumento solo puede contener letras y numeros.")
    public String numeroDocumento;

    @NotNull(message = "La clase de licencia es obligatoria")
    public String subClaseLicencia;

    public RenovacionLicenciaRequestDTO(String tipoDocumento, String numeroDocumento, String subClaseLicencia) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.subClaseLicencia = subClaseLicencia;
    }

}
