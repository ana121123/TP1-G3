package com.tpmetodosagiles.tpmetodosagiles.Repository;

import com.tpmetodosagiles.tpmetodosagiles.model.*;
import com.tpmetodosagiles.tpmetodosagiles.enums.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface ClaseRepository extends MongoRepository<Clase, String> {

    // Método para buscar una clase por su subcategoria
    @Query("{'subcategoria': ?0}")
    Clase findBySubcategoria(String subcategoria);

}
