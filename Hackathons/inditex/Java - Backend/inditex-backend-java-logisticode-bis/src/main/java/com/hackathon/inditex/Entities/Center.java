package com.hackathon.inditex.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "centers", uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude"}))
public class Center {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // El campo capacity que puede tener varios valores válidos como B, M, S, MS, BS, BMS
    private String capacity;

    private String status;

    private Integer currentLoad;

    private Integer maxCapacity;

    @Embedded
    private Coordinates coordinates;
    
    // Constructor por defecto y otros métodos podrían ir aquí si es necesario
}
