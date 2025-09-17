package com.tpmetodosagiles.tpmetodosagiles.Repository;

import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;
import com.tpmetodosagiles.tpmetodosagiles.model.*;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;

@Repository
// Repositorio para manejar las operaciones de UsuarioSistema en la base de
// datos MongoDB

public interface UsuarioSistemaRepository extends MongoRepository<UsuarioSistema, String> {
    // Metodo para buscar un usuario por su nombre de usuario
    @Query("{ 'username': ?0 }")
    UsuarioSistema findByUsername(String username);

    @Query("{ 'estado': 'Activo' , 'rol': 'ADMIN'}")
    List<UsuarioSistema> findUsuariosActivosAdministrarivos();

    // Metodo para buscar un usuario por su email
    @Query("{ 'email': ?0 }")
    UsuarioSistema findByEmail(String email);

    // Metodo para buscar todos los usuarios del sistema
    @Query("{}")
    java.util.List<UsuarioSistema> findAllUsuarios();

    // Método que devuelve si existe un Usuario de Sitema con ese username
    boolean existsByUsername(String userName);

    boolean existsByRol(Rol superusuario);

    long countByRolAndEstado(Rol rol, String estado);
}
