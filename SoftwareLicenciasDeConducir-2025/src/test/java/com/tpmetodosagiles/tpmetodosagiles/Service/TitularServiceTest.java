package com.tpmetodosagiles.tpmetodosagiles.Service;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.Repository.TitularRepository;
import com.tpmetodosagiles.tpmetodosagiles.model.Titular;

class TitularServiceTest {

    private LocalidadService localidadService;
    private TitularRepository titularRepository;
    private LicenciaService licenciaService;
    private TitularService titularService;

    @BeforeEach
    void setUp() {
        localidadService = mock(LocalidadService.class);
        titularRepository = mock(TitularRepository.class);
        licenciaService = mock(LicenciaService.class);
        //titularService = new TitularService(localidadService, titularRepository, licenciaService);
    }

    @Test
    void testRegisterTitular_WhenTitularIsNew_ShouldSaveSuccessfully() {
        // Arrange
        TitularRequestDTO request = new TitularRequestDTO();
        request.setNombre("Juan");
        request.setApellido("Perez");
        request.setTipoDocumento("DNI");
        request.setNumeroDocumento("12345678");
        request.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        request.setDireccion("Calle Falsa 123");
        request.setGrupoSanguineo("A");
        request.setFactorRH("+");
        request.setDonante(true);
        request.setCuit("20-12345678-3");
        request.setObservaciones("Ninguna");

        when(titularRepository.findByTipoYNumeroDocumento("DNI", "12345678")).thenReturn(null);

        // Capturamos el Titular que se guarda
        ArgumentCaptor<Titular> titularCaptor = ArgumentCaptor.forClass(Titular.class);
        when(titularRepository.save(titularCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TitularResponseDTO response = titularService.registerTitular(request);

        // Assert
        Titular savedTitular = titularCaptor.getValue();
        assertEquals("Juan", savedTitular.getNombre());
        assertEquals("Perez", savedTitular.getApellido());

        assertEquals("Juan", response.getNombre());
        assertEquals("Perez", response.getApellido());

        verify(titularRepository, times(1)).save(any(Titular.class));
    }

    @Test
    void testRegisterTitular_WhenTitularExists_ShouldThrowException() {
        // Arrange
        TitularRequestDTO request = new TitularRequestDTO();
        request.setTipoDocumento("DNI");
        request.setNumeroDocumento("12345678");

        Titular existingTitular = new Titular();
        when(titularRepository.findByTipoYNumeroDocumento("DNI", "12345678")).thenReturn(existingTitular);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            titularService.registerTitular(request);
        });

        assertEquals("Ya existe un titular con el mismo tipo y número de documento.", exception.getMessage());
        verify(titularRepository, never()).save(any());
    }
}
