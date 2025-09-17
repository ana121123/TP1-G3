package com.tpmetodosagiles.tpmetodosagiles.Service;

import org.springframework.stereotype.Service;
import com.tpmetodosagiles.tpmetodosagiles.model.Localidad;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LocalidadDTO;
import com.tpmetodosagiles.tpmetodosagiles.Repository.LocalidadRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocalidadService {

    @Autowired
    private LocalidadRepository localidadRepository;

    // Método para obtener todas las localidades
    public List<Localidad> obtenerTodasLasLocalidades() {
        return localidadRepository.findAllLocalidades();
    }

    // Método para buscar una localidad por su nombre
    public Localidad buscarLocalidadPorNombre(String nombre) {
        return localidadRepository.findByName(nombre);
    }

    // Metodo para mapToLocalidadDTO
    public LocalidadDTO mapToLocalidadDTO(Localidad localidad) {
        LocalidadDTO localidadDTO = new LocalidadDTO();
        localidadDTO.setId(localidad.getId());
        localidadDTO.setName(localidad.getName());
        return localidadDTO;
    }

    // Metodo para devolver una lista de todas las localidades en LocalidadDTO
    public List<LocalidadDTO> obtenerTodasLasLocalidadesDTO() {
        List<LocalidadDTO> localidades = new ArrayList<>();

        for (Localidad localidad : obtenerTodasLasLocalidades()) {
            localidades.add(mapToLocalidadDTO(localidad));
        }
        return localidades;
    }

    // Metodo para buscar una localidad por su nombre y devolverla como LocalidadDTO
    public LocalidadDTO buscarLocalidadPorNombreDTO(String nombre) {
        Localidad localidad = buscarLocalidadPorNombre(nombre);
        if (localidad != null) {
            return mapToLocalidadDTO(localidad);
        }
        return null;
    }

}
