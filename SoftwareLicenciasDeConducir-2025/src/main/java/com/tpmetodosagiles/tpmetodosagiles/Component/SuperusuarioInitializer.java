package com.tpmetodosagiles.tpmetodosagiles.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

@Component
public class SuperusuarioInitializer {

    @Autowired
    private UsuarioSistemaRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initSuperusuario() {
        boolean existe = usuarioRepository.existsByRol(Rol.SUPERUSUARIO)
                || usuarioRepository.existsByUsername("admin");

        if (!existe) {
            crearSuperusuario();
        }
    }

    private void crearSuperusuario() {
        UsuarioSistema admin = new UsuarioSistema();
        admin.setNombre("Super");
        admin.setApellido("Usuario");
        admin.setUsername("admin");
        //admin.setPassword("admin123");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRol(Rol.SUPERUSUARIO);
        admin.setEstado("Activo");

        usuarioRepository.save(admin);
    }
}