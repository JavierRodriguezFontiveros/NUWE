package com.hackathon.inditex.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String size;

    private String status;

    private String assignedLogisticsCenter;

    @Embedded
    private Coordinates coordinates;
}

