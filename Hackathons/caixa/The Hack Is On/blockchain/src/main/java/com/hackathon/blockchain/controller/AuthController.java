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
        // Pasamos el UserDTO directamente al servicio
        userService.registerUser(userDTO);
        return new ResponseMessage("Usuario registrado y logueado exitosamente");
    }

    // Login de usuario con LoginDTO
    @PostMapping("/login")
    public ResponseMessage login(@RequestBody LoginDTO loginDTO) {
        userService.loginUser(loginDTO);
        return new ResponseMessage("Login exitoso");
    }

    // Verificar sesión activa
    @GetMapping("/check-session")
    public ResponseMessage checkSession() {
        if (userService.checkSession()) {
            return new ResponseMessage("Sesión activa");
        }
        return new ResponseMessage("❌ Sesión no activa");
    }

    // Logout de usuario
    @PostMapping("/logout")
    public ResponseMessage logout() {
        userService.logoutUser();
        return new ResponseMessage("Logout exitoso");
    }
}
