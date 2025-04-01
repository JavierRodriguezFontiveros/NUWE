package com.hackathon.inditex.Entities;


public class OrderResponse {

    private Long id;
    private Long customerId;
    private String size;
    private String assignedLogisticsCenter; // Para el POST
    private Coordinates coordinates;
    private String message; // Solo para el POST
    private String status; // Solo para el POST
    private String assignedCenter; // Para el GET

    // Constructor para POST (con message y status)
    public OrderResponse(Order order, String message) {
        this.id = order.getId();
        this.customerId = order.getCustomerId();
        this.size = order.getSize();
        this.assignedLogisticsCenter = order.getAssignedLogisticsCenter(); // Para el POST
        this.coordinates = order.getCoordinates();
        this.message = message;  // Solo para el POST
        this.status = order.getStatus();  // Solo para el POST
    }

    // Constructor para GET (sin message ni status)
    public OrderResponse(Order order) {
        this.id = order.getId();
        this.customerId = order.getCustomerId();
        this.size = order.getSize();
        this.assignedCenter = order.getAssignedLogisticsCenter(); // Para el GET
        this.coordinates = order.getCoordinates();
    }

    // Getters y setters...

}
