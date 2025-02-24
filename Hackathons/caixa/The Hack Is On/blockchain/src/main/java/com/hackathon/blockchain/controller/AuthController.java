package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.service.UserService;
import com.hackathon.blockchain.dto.UserDTO;
import com.hackathon.blockchain.dto.LoginDTO;
import com.hackathon.blockchain.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Registro de usuario con UserDTO
    @PostMapping("/register")
    public ResponseMessage register(@RequestBody UserDTO userDTO) {
        // Pasamos los datos del UserDTO a UserService
        userService.registerUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        return new ResponseMessage("Usuario registrado y logueado exitosamente");
    }

    // Login de usuario con LoginDTO
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody LoginDTO loginDTO) {
        userService.loginUser(loginDTO.getUsername(), loginDTO.getPassword());
        return new ResponseMessage("Login exitoso");
    }

    // Verificar sesión activa
    @GetMapping("/check-session")
    public ResponseMessage checkSession(@RequestParam String username) {
        if (userService.checkSession(username)) {
            return new ResponseMessage("Sesión activa");
        }
        return new ResponseMessage("❌ Sesión no activa");
    }

    // Logout de usuario
    @PostMapping("/logout")
    public ResponseMessage logout(@RequestParam String username) {
        userService.logoutUser(username);
        return new ResponseMessage("Logout exitoso");
    }
}

