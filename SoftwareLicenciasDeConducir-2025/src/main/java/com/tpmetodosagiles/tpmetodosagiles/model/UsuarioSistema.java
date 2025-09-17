package com.tpmetodosagiles.tpmetodosagiles.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tpmetodosagiles.tpmetodosagiles.enums.Rol;

import lombok.*;

@Data
@NoArgsConstructor
@Document(collection = "usuarios")
public class UsuarioSistema implements UserDetails{
    @Id
    private String id;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private Rol rol;
    private String estado;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public UsuarioSistema(String nombre, String apellido, String username, String password, Rol rol, String estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    @Override
    public boolean isEnabled() {
        return !"INACTIVO".equalsIgnoreCase(this.estado);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // o implementá tu lógica si querés expiración
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // o según estado
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
