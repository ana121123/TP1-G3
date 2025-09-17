package com.tpmetodosagiles.tpmetodosagiles.Repository;

import com.tpmetodosagiles.tpmetodosagiles.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface LocalidadRepository extends MongoRepository<Localidad, String> {
    // Método para buscar una localidad por su name
    @Query("{ 'name': ?0 }")
    Localidad findByName(String name);

    // Método para buscar todas las localidades
    @Query("{}")
    java.util.List<Localidad> findAllLocalidades();
}