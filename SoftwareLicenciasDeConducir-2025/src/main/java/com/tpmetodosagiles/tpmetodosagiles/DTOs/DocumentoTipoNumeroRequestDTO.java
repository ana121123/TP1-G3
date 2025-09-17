package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DocumentoTipoNumeroRequestDTO {
    
    @NotBlank(message = "El tipoDocumento es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El tipoDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El tipoDocumento solo puede contener letras y espacios.")
    private String tipoDocumento; // DNI, Pasaporte, etc.

    @NotBlank(message = "El numeroDocumento es obligatorio y no puede estar vacío.")
    @Size(min = 7, max = 8, message = "El numeroDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El numeroDocumento solo puede contener letras y numeros.")
    private String numeroDocumento; // Número de documento
    
}