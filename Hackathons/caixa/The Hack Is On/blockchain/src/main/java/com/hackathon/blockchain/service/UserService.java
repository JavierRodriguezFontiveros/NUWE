package com.hackathon.blockchain.service;

import com.hackathon.blockchain.dto.UserDTO;
import com.hackathon.blockchain.dto.LoginDTO;
import com.hackathon.blockchain.model.User;
import com.hackathon.blockchain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;  // Inyectamos HttpSession para gestionar la sesión

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HttpSession session) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.session = session;
    }

    // Método para registrar un usuario
    public User registerUser(UserDTO userDTO) {
        // Verificar que el nombre de usuario no esté en uso
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        // Verificar que el correo electrónico no esté registrado
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        // Validar la fortaleza de la contraseña
        if (!userDTO.getPassword().matches(".*[A-Z].*") || !userDTO.getPassword().matches(".*\\d.*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one uppercase letter and one number");
        }

        // Crear un nuevo usuario y guardarlo
        User user = new User(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // Método para login
    public User loginUser(LoginDTO loginDTO) {
        // Buscar el usuario por el nombre de usuario
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Validar la contraseña
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        // Guardamos la sesión de usuario
        session.setAttribute("username", loginDTO.getUsername());
        return user;
    }

    // Verificar si la sesión está activa (verificando existencia de usuario)
    public boolean checkSession() {
        // Verificamos si el atributo 'username' existe en la sesión
        return session.getAttribute("username") != null;
    }

    // Logout de usuario (invalidar sesión)
    public void logoutUser() {
        // Invalidamos la sesión
        session.invalidate();
    }
}
