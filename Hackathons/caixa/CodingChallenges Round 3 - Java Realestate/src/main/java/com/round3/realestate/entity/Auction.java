package com.round3.realestate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property property;  // Relación con Property

    @Column(name = "current_highest_bid")
    private BigDecimal currentHighestBid;  // La oferta más alta actual

    @Column(name = "start_time")
    private LocalDateTime startTime;  // Hora de inicio de la subasta

    @Column(name = "end_time")
    private LocalDateTime endTime;  // Hora de finalización de la subasta

    @Column(name = "min_increment")
    private BigDecimal minIncrement;  // Incremento mínimo por oferta

    @Column(name = "starting_price")
    private BigDecimal startingPrice;  // Precio de inicio

    @Column(name = "status")
    private String status;  // Estado de la subasta (activa, finalizada, etc.)

    // Constructor vacío
    public Auction() {}

    // Constructor con parámetros
    public Auction(Property property, BigDecimal currentHighestBid, LocalDateTime startTime,
                   LocalDateTime endTime, BigDecimal minIncrement, BigDecimal startingPrice, String status) {
        this.property = property;
        this.currentHighestBid = currentHighestBid;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minIncrement = minIncrement;
        this.startingPrice = startingPrice;
        this.status = status;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public BigDecimal getCurrentHighestBid() {
        return currentHighestBid;
    }

    public void setCurrentHighestBid(BigDecimal currentHighestBid) {
        this.currentHighestBid = currentHighestBid;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getMinIncrement() {
        return minIncrement;
    }

    public void setMinIncrement(BigDecimal minIncrement) {
        this.minIncrement = minIncrement;
    }

    public BigDecimal getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
