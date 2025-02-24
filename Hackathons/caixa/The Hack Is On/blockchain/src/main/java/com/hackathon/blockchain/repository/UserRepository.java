package com.hackathon.blockchain.repository;

import com.hackathon.blockchain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Método personalizado para buscar por username
    Optional<User> findByUsername(String username);
    
    // Método personalizado para buscar por email
    Optional<User> findByEmail(String email);
}
