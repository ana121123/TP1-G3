package com.tpmetodosagiles.tpmetodosagiles.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tpmetodosagiles.tpmetodosagiles.Exceptions.AuthenticationException;
import com.tpmetodosagiles.tpmetodosagiles.Repository.UsuarioSistemaRepository;
import com.tpmetodosagiles.tpmetodosagiles.model.UsuarioSistema;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioSistemaRepository usuarioSistemaRepository;

    // Rutas que NO requieren autenticación
    private final List<String> publicPaths = Arrays.asList(
        "/api"
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extrae el token JWT del header Authorization
            String jwt = parseJwt(request);
            
            //si es una ruta publica, isPublicPath = true
            String requestPath = request.getRequestURI();
            boolean isPublicPath = publicPaths.stream()
                .anyMatch(path -> requestPath.startsWith(path));


            // Si existe token y es válido, autentica al usuario
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                UsuarioSistema user = usuarioSistemaRepository.findByUsername(username);
                if(user.getEstado()=="INACTIVO"){
                    sendInhabilitadoResponse(response, "Usuario inhabilitado");
                    return;
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // Crea el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            else{
                if(!isPublicPath && jwt == null){
                    sendUnauthorizedResponse(response, "Error de autenticación, Token nulo en url no publica");
                    return;
                }
            }
        } catch (Exception e) {
            sendUnauthorizedResponse(response, "Error de autenticación, token invalido: " + e.getClass() + ", " + e.getMessage());
            return;
        }

        // Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }
    
    // Extrae el token del header Authorization
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Remueve "Bearer " del inicio
        }
        
        return null;
    }

    // Envía respuesta de error 401 (No autorizado)
    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = String.format(
            "{\n\"error\": \"%s\"}", 
            message
        );
        
        response.getWriter().write(jsonResponse);
    }

    // Envía respuesta de error BAD_REQUEST (USUARIO INHABILITADO)
    private void sendInhabilitadoResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = String.format(
            "{\n\"error\": \"Unauthorized\",\n\"message\": \"%s\",\n \"status\": 401\n}", 
            message
        );
        
        response.getWriter().write(jsonResponse);
    }
}

