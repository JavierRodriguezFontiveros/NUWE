package com.round3.realestate.util;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.security.Key;

public class JwtUtil {

    private Key secretKey;

    public JwtUtil(String encodedSecret) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedSecret); // Decodificando la clave secreta Base64
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);  // Usando la clave decodificada para firmar los JWTs
    }

    // Método para generar el JWT
    public String generateToken(Map<String, Object> claims, Long userId, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId)) // El ID del usuario como el "subject"
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // Expira en el tiempo configurado
                .signWith(secretKey, SignatureAlgorithm.HS512) // Firma el token con la clave secreta
                .compact();
    }

    // Método para extraer el ID de usuario desde el JWT
    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return Long.parseLong(claims.getSubject());
    }

    // Método para validar el token
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Comprobar si el token ha expirado
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;  // Si ocurre una excepción, el token es inválido
        }
    }

    // Método para extraer las reclamaciones (claims) del token
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
