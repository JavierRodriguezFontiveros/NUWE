package com.round3.realestate.service;

import com.round3.realestate.entity.User;
import com.round3.realestate.repository.UserRepository;
import com.round3.realestate.service.CustomUserDetails;  // Asegúrate de importar esta clase
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Buscar el usuario por su nombre de usuario
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Ahora no necesitas verificar el rol ya que no existe en el modelo
        // Retorna los detalles del usuario sin el rol
        return new CustomUserDetails(user.getUsername(), user.getPassword(), user.getEmail());
    }
}
