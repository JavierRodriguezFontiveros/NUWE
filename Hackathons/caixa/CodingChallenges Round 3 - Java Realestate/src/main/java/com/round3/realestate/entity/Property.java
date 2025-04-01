package com.round3.realestate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.math.BigDecimal;
import jakarta.persistence.Table;

@Entity
@Table(name = "properties")  // Aquí estamos definiendo el nombre de la tabla
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "availability")
    private String status;  // Estado de la propiedad, puede ser 'Available', 'Sold', etc.

    @Column(name = "location")
    private String location;  // Ubicación de la propiedad

    @Column(name = "name")
    private String type;  // Aquí almacenaremos el tipo de propiedad (Ej: "Casa", "Chalet")

    @Column(name = "price")
    private BigDecimal price;  // Precio de la propiedad

    @Column(name = "rooms")
    private String rooms;  // Número de habitaciones

    @Column(name = "size")
    private String size;  // Tamaño de la propiedad

    // Constructor vacío
    public Property() {}

    // Constructor con parámetros
    public Property(String location, BigDecimal price, String size, String rooms, String status) {
        this.location = location;
        this.price = price;
        this.size = size;
        this.rooms = rooms;
        this.status = status;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;  // Aquí asignamos el tipo de propiedad
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRooms() {
        return rooms;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
