package com.tpmetodosagiles.tpmetodosagiles.Service;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.CrearUsuarioDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.ModificarUsuarioDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.UsuarioSistemaDTO;
import com.tpmetodosagiles.tpmetodosagiles.Exceptions.MapIllegalArgumentException;
import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

import jakarta.annotation.Nonnull;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioSistemaRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UsuarioSistema crearUsuario(CrearUsuarioDTO dto){
        return crearUsuario(dto.getUsername(), dto.getPassword(), dto.getApellido(), dto.getNombre());
    }
    // Crea un nuevo usuario con contraseña hasheada
    public UsuarioSistema crearUsuario(String username, String password, String apellido, String nombre) {
        // Verifica si el usuario ya existe
        if (usuarioRepository.existsByUsername(username)) {
            throw new MapIllegalArgumentException("username",
                    "username ya existente");
        }

        String hashedPassword = passwordEncoder.encode(password);
        
        // Crea el usuario
        UsuarioSistema usuario = new UsuarioSistema(nombre, apellido, username, hashedPassword, Rol.ADMIN, "Activo");
                
        // Guarda en MongoDB
        return usuarioRepository.save(usuario);
    }

    public UsuarioSistema modificarUsuario(ModificarUsuarioDTO dto){
        UsuarioSistema useroriginal = usuarioRepository.findByUsername(dto.getUsernameSinModificar());
        // Verifica si el usuario ya existe
        if (useroriginal == null) {
             throw new MapIllegalArgumentException("username",
                    "username no existente");
        }
        if(dto.getPassword() != ""){
            useroriginal.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
            
        
        
        // Crea el usuario
        useroriginal.setNombre(dto.getNombre());
        useroriginal.setApellido(dto.getApellido());
        useroriginal.setUsername(dto.getUsername());
                
        // Guarda en MongoDB
        return usuarioRepository.save(useroriginal);
    }
    
    public UsuarioSistema obtenerUsuario(String username) {
        UsuarioSistema usuario = usuarioRepository.findByUsername(username);
        if(usuario == null)
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        return usuario;
    }

    // Dar de baja con reglas especiales
    public void darDeBajaUsuario(@Nonnull String username/*, @Nonnull UsuarioSistema usuarioAutenticado*/) {
        // Verificar que el autenticado sea SUPERUSUARIO
        /*if (!usuarioAutenticado.getRol().equals(Rol.SUPERUSUARIO)) {
            throw new SecurityException("No autorizado: se requiere rol SUPERUSUARIO");
        }

        // No permitir autodesactivación
        if (usuarioAutenticado.getId().equals(id)) {
            throw new IllegalArgumentException("No se puede dar de baja a sí mismo");
        }*/

        // Buscar usuario a dar de baja
        UsuarioSistema usuarioABaja = usuarioRepository.findByUsername(username);

        // Realizar baja lógica
        usuarioABaja.setEstado("INACTIVO");
        usuarioRepository.save(usuarioABaja);
    }

    public List<UsuarioSistemaDTO> listarUsuarios(){
        return usuarioRepository.findUsuariosActivosAdministrarivos()
                                .stream()
                                .map(usuario -> new UsuarioSistemaDTO(
                                    usuario.getNombre(),
                                    usuario.getApellido(),
                                    usuario.getUsername(),
                                    usuario.getPassword()
                                ))
                                .collect(Collectors.toList());
    }
}
