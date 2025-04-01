package com.round3.realestate.service;

import com.round3.realestate.entity.User;
import com.round3.realestate.repository.UserRepository;
import com.round3.realestate.util.JwtUtil;  // Importa la clase JwtUtil
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;  // Inyecta JwtUtil

    @Value("${app.jwtSecret}")
    private String jwtSecret; // Clave secreta Base64 codificada

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpiration;

    // Registro de un nuevo usuario
    public String registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            logger.warn("El nombre de usuario {} ya existe", username);
            throw new RuntimeException("The username already exists");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("El correo electrónico {} ya existe", email);
            throw new RuntimeException("Email already exists");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setStatus("unemployed"); // Estado por defecto

        userRepository.save(user);

        logger.info("Usuario registrado exitosamente: {}", username);
        return "User successfully registered";
    }

    // Inicio de sesión de un usuario
    public Map<String, String> loginUser(String usernameOrEmail, String password) {
        Optional<User> user = userRepository.findByUsername(usernameOrEmail);

        if (!user.isPresent()) {
            logger.info("Usuario no encontrado por nombre de usuario, buscando por correo electrónico");
            user = userRepository.findByEmail(usernameOrEmail); // Buscar por email si no se encuentra por username
        }

        if (user.isPresent()) {
            logger.info("Usuario encontrado: {}", user.get().getUsername());
            if (passwordEncoder.matches(password, user.get().getPassword())) {
                // Eliminar verificación de estado y rol del usuario
                logger.info("Credenciales correctas, generando JWT para el usuario: {}", user.get().getUsername());
                
                // Crear la respuesta con solo el accessToken y el tokenType
                Map<String, String> response = new HashMap<>();
                response.put("accessToken", jwtUtil.generateToken(
                        new HashMap<>(),  // Puedes agregar claims personalizados aquí si es necesario
                        user.get().getId(),
                        jwtExpiration
                ));
                response.put("tokenType", "Bearer");
                return response;  // No agregamos "success" a la respuesta
            } else {
                logger.warn("Contraseña incorrecta para el usuario: {}", usernameOrEmail);
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            logger.warn("Usuario no encontrado: {}", usernameOrEmail);
            throw new RuntimeException("Invalid credentials");
        }
    }
}
