package com.round3.realestate.controller;

import com.round3.realestate.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    // Endpoint de registro
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            logger.info("Solicitud de registro para usuario: {}", registerRequest.getUsername());
            String message = authService.registerUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword());
            return ResponseEntity.ok(new ApiResponse(true, message));
        } catch (RuntimeException e) {
            logger.error("Error en el registro: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Solicitud de inicio de sesión para: {}", loginRequest.getUsernameOrEmail());
            
            // Cambiar el retorno de loginUser para recibir Map<String, String>
            Map<String, String> response = authService.loginUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());

            // Retorna la respuesta con el accessToken y tokenType como un Map
            return ResponseEntity.ok(response);  // Devuelve el Map tal como lo retorna el servicio
        } catch (RuntimeException e) {
            logger.warn("Error en el login: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse(false, "Unauthorized: " + e.getMessage()));
        }
    }


    // Respuesta común para el registro y login
    public static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    // Respuesta para login con JWT
    public static class LoginResponse {
        private boolean success;
        private String accessToken;
        private String tokenType;

        public LoginResponse(boolean success, String accessToken, String tokenType) {
            this.success = success;
            this.accessToken = accessToken;
            this.tokenType = tokenType;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getAccessToken() { return accessToken; }
        public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    }

    // Clase para solicitud de login
    public static class LoginRequest {
        private String usernameOrEmail;
        private String password;

        public String getUsernameOrEmail() { return usernameOrEmail; }
        public void setUsernameOrEmail(String usernameOrEmail) { this.usernameOrEmail = usernameOrEmail; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Clase para solicitud de registro
    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}