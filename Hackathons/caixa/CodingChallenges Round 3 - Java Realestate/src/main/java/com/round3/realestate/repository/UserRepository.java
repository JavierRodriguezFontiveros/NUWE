package com.round3.realestate.repository;

import com.round3.realestate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // Buscar por correo electrónico
    Optional<User> findByUsername(String username); // Buscar por nombre de usuario
}