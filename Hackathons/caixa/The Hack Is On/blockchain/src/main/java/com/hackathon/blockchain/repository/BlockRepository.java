package com.hackathon.blockchain.repository;

import com.hackathon.blockchain.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    // Método para obtener el último bloque basado en el índice de bloque
    Optional<Block> findTopByOrderByBlockIndexDesc();
}
