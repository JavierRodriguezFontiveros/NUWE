package com.round3.realestate.filter;

import com.round3.realestate.service.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private Key signingKey;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    public JwtRequestFilter(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = getJwtFromRequest(request);
        if (token != null) {
            try {
                Claims claims = validateToken(token);
                if (claims != null) {
                    String username = claims.getSubject();
                    logger.debug("Token validado con éxito para el usuario: {}", username);

                    UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("Autenticación configurada para el usuario: {}", username);
                }
            } catch (ExpiredJwtException e) {
                logger.error("Token expirado: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado.");
                return;
            } catch (JwtException | IllegalArgumentException e) {
                logger.error("Token inválido: {}", e.getMessage());
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido.");
                return;
            }
        } else {
            logger.warn("No se recibió ningún token en la solicitud.");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "No se recibió token.");
            return;
        }

        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Claims validateToken(String token) throws JwtException {
        // Verifica si la clave no está configurada correctamente.
        if (signingKey == null) {
            throw new IllegalStateException("La clave de firma JWT no está configurada correctamente.");
        }

        try {
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = jwsClaims.getBody();
            if (claims.getExpiration().before(new Date())) {
                throw new ExpiredJwtException(null, claims, "El token ha expirado.");
            }

            return claims;
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(e.getHeader(), e.getClaims(), e.getMessage());
        } catch (JwtException e) {
            logger.error("Error en la validación del token: {}", e.getMessage());
            throw e;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    // Método para configurar la clave al iniciar el filtro
    @Value("${app.jwtSecret}")
    private void configureSigningKey(String secret) {
        try {
            logger.debug("Configurando la clave secreta...");
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            if (decodedKey.length != 64) {
                throw new IllegalArgumentException("La clave JWT debe ser de 64 bytes para HS512");
            }
            this.signingKey = Keys.hmacShaKeyFor(decodedKey);
            logger.debug("Clave secreta configurada correctamente.");
        } catch (Exception e) {
            logger.error("Error al configurar la clave JWT: {}", e.getMessage());
            throw new IllegalArgumentException("Clave JWT inválida", e);
        }
    }
}
