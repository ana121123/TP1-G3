package com.tpmetodosagiles.tpmetodosagiles.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;

import lombok.Data;

/*tipo y número de documento, nombre, apellido, 
fecha de nacimiento, dirección
grupo sanguíneo, factor RH, condición de donante, CUIT y observaciones */
@Data
@Document(collection = "titular")
public class Titular {

    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String tipoDocumento; // DNI, Pasaporte, etc.
    private String numeroDocumento; // Número de documento
    private LocalDate fechaNacimiento;
    private String direccion;
    private String grupoSanguineo; // A, B, AB, O
    private String factorRH; // Positivo o Negativo
    private Boolean donante; // Condición de donante
    private String cuit;
    private String observaciones;

    private Localidad localidad; // Localidad del titular que figura en el dni

    @org.springframework.data.mongodb.core.mapping.Field("licenciasVigentes")
    private List<Licencia> licenciasVigentes;

    @org.springframework.data.mongodb.core.mapping.Field("historialLicencias")
    private List<Licencia> historialLicencias;

    // Set y get para las listas de licencias
    public List<Licencia> getLicenciasVigentes() {
        return licenciasVigentes;
    }

    public void setLicenciasVigentes(List<Licencia> licenciasVigentes) {
        this.licenciasVigentes = licenciasVigentes;
    }

    public List<Licencia> getHistorialLicencias() {
        return historialLicencias;
    }

    public void setHistorialLicencias(List<Licencia> historialLicencias) {
        this.historialLicencias = historialLicencias;
    }

    // ingresar una licencia vencida al historial de licencias
    public void agregarLicenciaVencidaAlHistorial(Licencia licencia) {
        if (historialLicencias == null) {
            historialLicencias = new ArrayList<>();
        }
        licenciasVigentes.remove(licencia);
        licencia.setEstado("Vencida");
        historialLicencias.add(licencia);
    }

    // ingresar una licencia vigente a la lista de licencias vigentes
    public void agregarLicenciaVigente(Licencia licencia, boolean moverAHistorial) {
        if (moverAHistorial) {
            moverLicenciaVigenteAHistorial(licencia.getClaseString());
        }
        if (licenciasVigentes == null) {
            licenciasVigentes = new ArrayList<>();
        }
        licencia.setEstado("Vigente");
        licenciasVigentes.add(licencia);
    }

    // Método para calcular la edad del titular a partir de su fecha de nacimiento
    public int calcularEdad() {
        if (fechaNacimiento == null) {
            return 0;
        }
        return java.time.Period.between(fechaNacimiento, java.time.LocalDate.now()).getYears();
    }

    // Método para calcular la edad del titular a partir de su fecha de nacimiento
    public int calcularEdad(LocalDate fechaParaCalcularEdad) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return java.time.Period.between(fechaNacimiento, fechaParaCalcularEdad).getYears();
    }

    // Método para verificar si pasaron mas de 3 meses de la fecha del cumpleaños
    public boolean esCumpleanosReciente() {
        if (fechaNacimiento == null) {
            return false;
        }
        LocalDate fechaCumpleanos = LocalDate.of(LocalDate.now().getYear(), fechaNacimiento.getMonth(),
                fechaNacimiento.getDayOfMonth());
        return fechaCumpleanos.isAfter(LocalDate.now().minusMonths(3)) && fechaCumpleanos.isBefore(LocalDate.now());
    }

    public void moverLicenciaVigenteAHistorial(String subClase) {
        // Buscar la licencia vigente de la clase especificada
        if (this.licenciasVigentes.isEmpty()) {
            return; // No hay licencias vigentes para mover
        }
        Licencia licenciaVigente = licenciasVigentes.stream()
                .filter(licencia -> licencia.getClaseString().equals(subClase))
                .findFirst()
                .orElse(null);

        // Si se encuentra la licencia, moverla al historial
        if (licenciaVigente != null) {
            agregarLicenciaVencidaAlHistorial(licenciaVigente);
        }

    }

    public boolean faltanMeneosDeNmesesParaSuCumpleños(int meses) {
        if (fechaNacimiento == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaCumpleanos = LocalDate.of(hoy.getYear(), fechaNacimiento.getMonth(),
                fechaNacimiento.getDayOfMonth());

        if (fechaCumpleanos.isBefore(hoy)) {
            fechaCumpleanos = fechaCumpleanos.plusYears(1);
        }

        return !fechaCumpleanos.isBefore(hoy) && fechaCumpleanos.isBefore(hoy.plusMonths(meses));
    }

    public boolean PasaronMenosDeNmesesDesdeSuCumpleaños(int meses) {
        if (fechaNacimiento == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaCumpleanos = LocalDate.of(hoy.getYear(), fechaNacimiento.getMonth(),
                fechaNacimiento.getDayOfMonth());

        if (fechaCumpleanos.isAfter(hoy)) {
            fechaCumpleanos = fechaCumpleanos.minusYears(1);
        }

        return !fechaCumpleanos.isAfter(hoy) && fechaCumpleanos.isAfter(hoy.minusMonths(meses));
    }

}
