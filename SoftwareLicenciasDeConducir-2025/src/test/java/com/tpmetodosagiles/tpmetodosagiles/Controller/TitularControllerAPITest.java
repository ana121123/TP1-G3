package com.tpmetodosagiles.tpmetodosagiles.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.TitularResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.Service.TitularService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TitularControllerAPI.class)
public class TitularControllerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TitularService titularService;

    @Autowired
    private ObjectMapper objectMapper;

    private TitularRequestDTO getValidTitularDTO() {
        TitularRequestDTO dto = new TitularRequestDTO();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setTipoDocumento("DNI");
        dto.setNumeroDocumento("12345678");
        dto.setFechaNacimiento(LocalDate.of(1990, 5, 1));
        dto.setDireccion("Calle Falsa 123");
        dto.setGrupoSanguineo("A");
        dto.setFactorRH("Positivo");
        dto.setDonante(true);
        dto.setCuit("20123456789");
        dto.setObservaciones("Observaciones varias.");
        return dto;
    }

    @Test
    public void crearTitular_ValidRequest_ReturnsOk() throws Exception {
        TitularRequestDTO request = getValidTitularDTO();

        TitularResponseDTO response = new TitularResponseDTO();
        response.setNombre(request.getNombre());
        response.setApellido(request.getApellido());
        // ... (completar con otros setters si querés validarlo todo)

        when(titularService.registerTitular(any())).thenReturn(response);

        mockMvc.perform(post("/api/titular/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Juan"))
            .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    @Test
    public void crearTitular_InvalidRequest_ReturnsBadRequest() throws Exception {
        TitularRequestDTO request = getValidTitularDTO();
        request.setNombre(""); // inválido por @NotBlank y @Size

        mockMvc.perform(post("/api/titular/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    public void crearTitular_DuplicatedTitular_ReturnsBadRequest() throws Exception {
        TitularRequestDTO request = getValidTitularDTO();

        when(titularService.registerTitular(any()))
                .thenThrow(new IllegalArgumentException("Ya existe un titular con el mismo tipo y número de documento."));

        mockMvc.perform(post("/api/titular/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.documento").value("Ya existe un titular con el mismo tipo y número de documento."));
    }
}
