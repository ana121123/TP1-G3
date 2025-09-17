package com.tpmetodosagiles.tpmetodosagiles.Controller;

import com.tpmetodosagiles.tpmetodosagiles.DTOs.CrearUsuarioDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LoginRequestDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.LoginResponseDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.ModificarUsuarioDTO;
import com.tpmetodosagiles.tpmetodosagiles.DTOs.UsuarioSistemaDTO;
import com.tpmetodosagiles.tpmetodosagiles.Security.JwtUtils;
import com.tpmetodosagiles.tpmetodosagiles.Service.UsuarioService;
import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UsuarioService usuarioService;

    // ---------- LOGIN ----------
    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UsuarioSistema usuario = (UsuarioSistema) authentication.getPrincipal();

            LoginResponseDTO response = new LoginResponseDTO(
                jwt,
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol().toString()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Credenciales inválidas");
        }
    }

    // ---------- CREAR ----------
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody CrearUsuarioDTO dto) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UsuarioSistema usuarioAutenticado = (UsuarioSistema) authentication.getPrincipal();

        /*if (!usuarioAutenticado.getRol().equals(Rol.SUPERUSUARIO)) {
            return ResponseEntity.status(403).body("No autorizado: se requiere rol SUPERUSUARIO");
        }*/

        
        usuarioService.crearUsuario(dto);
        return ResponseEntity.ok("Usuario creado correctamente");
        
    }
    
    // ---------- MODIFICAR ----------
    @PutMapping("/usuarios/modificar")
    public ResponseEntity<?> modificarUsuario(@RequestBody ModificarUsuarioDTO dto) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UsuarioSistema usuarioAutenticado = (UsuarioSistema) authentication.getPrincipal();

        /*if (!usuarioAutenticado.getRol().equals(Rol.SUPERUSUARIO)) {
            return ResponseEntity.status(403).body("No autorizado: se requiere rol SUPERUSUARIO");
        }*/

        
            usuarioService.modificarUsuario(dto);
            return ResponseEntity.ok("Usuario mofificado correctamente");
    }

    // ---------- BAJA LÓGICA ----------
    @PutMapping("/usuarios/baja")
    public ResponseEntity<?> darDeBajaUsuario(@RequestParam String username) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UsuarioSistema usuarioAutenticado = (UsuarioSistema) authentication.getPrincipal();

        /*if (!usuarioAutenticado.getRol().equals(Rol.SUPERUSUARIO)) {
            return ResponseEntity.status(403).body("No autorizado: se requiere rol SUPERUSUARIO");
        }*/

        try {
            usuarioService.darDeBajaUsuario(username/*, usuarioAutenticado*/);
            return ResponseEntity.ok("Usuario dado de baja correctamente (estado: INACTIVO)");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/listar")
    public ResponseEntity<?> listarUsuarios() {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //UsuarioSistema usuarioAutenticado = (UsuarioSistema) authentication.getPrincipal();

        /*if (!usuarioAutenticado.getRol().equals(Rol.SUPERUSUARIO)) {
            return ResponseEntity.status(403).body("No autorizado: se requiere rol SUPERUSUARIO");
        }*/

        
        List<UsuarioSistemaDTO> usuarios = usuarioService.listarUsuarios();
        if(usuarios == null || usuarios.isEmpty()) return new ResponseEntity<>(usuarios, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
}
