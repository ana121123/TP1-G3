package com.tpmetodosagiles.tpmetodosagiles.Service;

import com.tpmetodosagiles.tpmetodosagiles.enums.ClaseLicencia;
import com.tpmetodosagiles.tpmetodosagiles.enums.CostoLicencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Clase;
import com.tpmetodosagiles.tpmetodosagiles.model.Licencia;
import com.tpmetodosagiles.tpmetodosagiles.model.Titular;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LicenciaServiceTest {

    @InjectMocks
    private LicenciaService licenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Titular crearTitularConEdad(int edad, boolean cumpleEl29Feb) {
        Titular titular = new Titular();
        LocalDate hoy = LocalDate.of(2025, 6, 8);
        LocalDate fechaNacimiento = cumpleEl29Feb
                ? LocalDate.of(hoy.minusYears(edad).getYear(), 2, 29)
                : hoy.minusYears(edad).withDayOfYear(1);
        titular.setFechaNacimiento(fechaNacimiento);
        return titular;
    }

    private Licencia crearLicencia(ClaseLicencia claseLicencia) {
        Licencia licencia = new Licencia();
        Clase clase = new Clase();
        clase.setCategoria(claseLicencia);
        licencia.setClase(clase);
        return licencia;
    }

    @Test
    void testCostoClaseA() {
        Licencia licencia = crearLicencia(ClaseLicencia.A);
        Titular titular = crearTitularConEdad(25, false);
        int esperado = CostoLicencia.obtenerCostoTotal("A", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseB() {
        Licencia licencia = crearLicencia(ClaseLicencia.B);
        Titular titular = crearTitularConEdad(22, false);
        int esperado = CostoLicencia.obtenerCostoTotal("B", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseC() {
        Licencia licencia = crearLicencia(ClaseLicencia.C);
        Titular titular = crearTitularConEdad(30, false);
        int esperado = CostoLicencia.obtenerCostoTotal("C", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseD() {
        Licencia licencia = crearLicencia(ClaseLicencia.D);
        Titular titular = crearTitularConEdad(20, false);
        int esperado = CostoLicencia.obtenerCostoTotal("D", 3);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, true, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseE() {
        Licencia licencia = crearLicencia(ClaseLicencia.E);
        Titular titular = crearTitularConEdad(24, false);
        int esperado = CostoLicencia.obtenerCostoTotal("E", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseF() {
        Licencia licencia = crearLicencia(ClaseLicencia.F);
        Titular titular = crearTitularConEdad(18, false);
        int esperado = CostoLicencia.obtenerCostoTotal("F", 1);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoClaseG() {
        Licencia licencia = crearLicencia(ClaseLicencia.G);
        Titular titular = crearTitularConEdad(26, false);
        int esperado = CostoLicencia.obtenerCostoTotal("G", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testCostoCumple29Febrero() {
        Licencia licencia = crearLicencia(ClaseLicencia.B);
        Titular titular = crearTitularConEdad(25, true);
        int esperado = CostoLicencia.obtenerCostoTotal("B", 5);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, false, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testRenovacionReducidaPorEdadMayor65() {
        Licencia licencia = crearLicencia(ClaseLicencia.B);
        Titular titular = crearTitularConEdad(68, false);
        int esperado = CostoLicencia.obtenerCostoTotal("B", 3);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, true, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testRenovacionEdadMediana() {
        Licencia licencia = crearLicencia(ClaseLicencia.C);
        Titular titular = crearTitularConEdad(53, false);
        int esperado = CostoLicencia.obtenerCostoTotal("C", 4);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, true, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testRenovacionJoven() {
        Licencia licencia = crearLicencia(ClaseLicencia.A);
        Titular titular = crearTitularConEdad(75, false);
        int esperado = CostoLicencia.obtenerCostoTotal("A", 1);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, true, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }

    @Test
    void testRenovacion29FebreroMayor70() {
        Licencia licencia = crearLicencia(ClaseLicencia.D);
        Titular titular = crearTitularConEdad(73, true);
        int esperado = CostoLicencia.obtenerCostoTotal("D", 1);
        int actual = licenciaService.calcularCostoTotal(licencia, titular, true, LocalDate.of(2025, 6, 8));
        assertEquals(esperado, actual);
    }
}
