package com.tpmetodosagiles.tpmetodosagiles.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;

import lombok.Data;

@Data
public class Licencia {
    @Id
    private String id;
    private String observaciones;
    private Clase clase;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private String estado; // Activa, Inactiva, Expirada
    private int costo;
    private Localidad localidadLicencia; // Localidad donde se emite la licencia

    @DBRef(lazy = true)
    private UsuarioSistema usuarioSistema;

    // Constructor, getters y setters pueden ser generados automáticamente por
    // Lombok
    public String getClaseString() {
        return clase.getCategoria().getNombre();
    }

    // Método para calcular cuanto tiempo falta para que la licencia venza
    public int calcularDiasParaVencimiento() {
        // Implementar lógica para calcular los días restantes hasta el vencimiento
        // de la licencia
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }
}
