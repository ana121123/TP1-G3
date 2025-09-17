package com.tpmetodosagiles.tpmetodosagiles.DTOs;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TitularRequestDTO {
    @NotBlank(message = "El nombre es obligatorio y no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios.")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio y no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El apellido solo puede contener letras y espacios.")
    private String apellido;

    @NotBlank(message = "El tipoDocumento es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El tipoDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El tipoDocumento solo puede contener letras y espacios.")
    private String tipoDocumento; // DNI, Pasaporte, etc.

    @NotBlank(message = "El numeroDocumento es obligatorio y no puede estar vacío.")
    @Size(min = 7, max = 8, message = "El numeroDocumento puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El numeroDocumento solo puede contener letras y numeros.")
    private String numeroDocumento; // Número de documento

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    // @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha de nacimiento
    // debe estar en formato YYYY-MM-DD.")
    @Past(message = "La fecha de nacimiento debe ser anterior a la actual.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La direccion es obligatoria y no puede estar vacía.")
    @Size(max = 50, message = "La direccion puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]+$", message = "La direccion solo puede contener letras, espacios y numeros.")
    private String direccion;

    @NotBlank(message = "El grupo sanguíneo es obligatorio.")
    @Pattern(regexp = "^(A|B|AB|O)$", message = "Grupo sanguíneo inválido. Valores permitidos: A, B, AB, O.")
    private String grupoSanguineo; // A, B, AB, O

    @NotBlank(message = "El factorRH es obligatorio.")
    @Pattern(regexp = "^(Positivo|Negativo)$", message = "factorRH inválido. Valores permitidos: Positivo, Negativo.")
    private String factorRH; // Positivo o Negativo

    @NotNull(message = "La condicion de donante es obligatoria.")
    // @Pattern(regexp = "^(true|false)$", message = "El valor del campo 'donante'
    // debe ser 'true' o 'false'.")
    private Boolean donante; // Condición de donante

    @NotBlank(message = "El cuit es obligatorio y no puede estar vacío.")
    @Size(max = 50, message = "El cuit puede tener hasta 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$", message = "El cuit solo puede contener letras y numeros.")
    private String cuit;

    @NotNull(message = "El campo observacion no puede ser null.")
    @Size(max = 500, message = "Las observaciones puede tener hasta 500 caracteres.")
    private String observaciones;

    @NotBlank(message = "El campo localidad no puede estar vacío.")
    @Size(max = 100, message = "La localidad puede tener hasta 100 caracteres.")
    private String localidad;

    /*
     * @NotBlank(message =
     * "La fechaNacimiento es obligatoria y no puede estar vacía.")
     * 
     * @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message =
     * "La fecha de nacimiento debe estar en formato YYYY-MM-DD.")
     * //la validacion que restringe que la fecha sea anterior a la actual se
     * realiza en el metodo getFechaNacimientoLocalDate() más abajo
     * private String fechaNacimiento_string;
     * 
     * @Past(message = "La fecha de nacimiento debe ser en el pasado.")
     * public LocalDate getFechaNacimientoLocalDate() {
     * return LocalDate.parse(this.fechaNacimiento_string);
     * }
     */
}
