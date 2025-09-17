package com.tpmetodosagiles.tpmetodosagiles.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

@Service
public class UsuarioDetailService implements UserDetailsService {
    
    @Autowired
    private UsuarioSistemaRepository usuarioRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos
        UsuarioSistema usuario = usuarioRepository.findByUsername(username);
        if(usuario == null)
            new UsernameNotFoundException("Usuario no encontrado: " + username);
        
        return usuario; // Retorna el usuario (que implementa UserDetails)
    }
}
