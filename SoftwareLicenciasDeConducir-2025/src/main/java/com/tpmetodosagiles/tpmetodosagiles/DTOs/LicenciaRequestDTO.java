package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;
import com.tpmetodosagiles.tpmetodosagiles.model.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
public class LicenciaRequestDTO {

    // no se está usando
    private Boolean isRenewal;

    @NotNull(message = "La clase es obligatoria.")
    private ClaseRequestDTO clase;

    @NotNull(message = "El campo observacion no puede ser null.")
    @Size(max = 500, message = "Las observaciones puede tener hasta 500 caracteres.")
    private String observaciones;

    @NotBlank(message = "El tipoDocumento es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El tipoDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El tipoDocumento solo puede contener letras y espacios.")
    private String tipoDocumento; // Titular

    @NotBlank(message = "El numeroDocumento es obligatorio y no puede estar vacío.")
    @Size(min = 7, max = 8, message = "El numeroDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El numeroDocumento solo puede contener letras y numeros.")
    private String numeroDocumento;// Titular

    @NotNull(message = "El campo edadTitular no puede ser null.")
    private int edadTitular;

    @NotBlank(message = "El username es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El username puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El username solo puede contener letras y numeros.")
    private String username; // UsuarioSistema que registra la licencia

    @NotBlank(message = "La localidad es obligatoria y no puede estar vacía.")
    @Size(max = 100, message = "La localidad puede tener hasta 100 caracteres.")
    private String localidad;

    // Metodo que traduce el enum a un String
    public String getClaseString() {
        return clase.getCategoria().getNombre();
    }

    // Constructor, getters y setters pueden ser generados automáticamente por
    // Lombok
}