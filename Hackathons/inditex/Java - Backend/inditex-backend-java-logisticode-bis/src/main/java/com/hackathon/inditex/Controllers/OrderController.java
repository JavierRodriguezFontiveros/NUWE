package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Services.OrderService;
import com.hackathon.inditex.Entities.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Crear un nuevo pedido con validaciones mejoradas
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            // Al crear el pedido, devolvemos el status junto con assignedLogisticsCenter y message
            OrderResponse response = orderService.createOrder(order);
            return ResponseEntity.status(201).body(response); // 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage()); // 400 Bad Request si hay error
        }
    }

    // Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        // Solo devolvemos el assignedCenter, sin message ni status
        List<OrderResponse> orderResponses = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponses);
    }

    // Endpoint para asignar centros logísticos a los pedidos pendientes
    @PostMapping("/assign")
    public ResponseEntity<?> assignCenters() {
        String response = orderService.assignCentersToOrders();
        return ResponseEntity.ok(response);
    }
}
