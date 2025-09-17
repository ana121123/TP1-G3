package com.tpmetodosagiles.tpmetodosagiles.Repository;

import com.tpmetodosagiles.tpmetodosagiles.model.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

@Repository
public interface TitularRepository extends MongoRepository<Titular, String> {

    @Query("{ 'tipoDocumento': ?0, 'numeroDocumento': ?1 }")
    Titular findByTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento);

    @Query("{'nombre': ?0, 'apellido': ?1 }")
    Titular findByNombreYApellido(String nombre, String apellido);

    // Metodo para buscar todos los titulares
    @Query("{}")
    java.util.List<Titular> findAllTitulares();

}
