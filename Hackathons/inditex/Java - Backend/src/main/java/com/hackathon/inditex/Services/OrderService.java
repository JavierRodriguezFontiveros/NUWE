package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Entities.Order;
import com.hackathon.inditex.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hackathon.inditex.Entities.OrderResponse;
import java.util.List;
import java.util.stream.Collectors;  // Importar Collectors para transformar la lista

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CenterService centerService; // Usamos el CenterService para gestionar centros logísticos

    private static final String[] VALID_SIZES = {"B", "M", "S"};

    private boolean isValidSize(String size) {
        for (String validSize : VALID_SIZES) {
            if (validSize.equals(size)) {
                return true;
            }
        }
        return false;
    }

    // ✅ Crear un nuevo pedido con validaciones estrictas
    public OrderResponse createOrder(Order order) {
        if (order.getSize() == null || !isValidSize(order.getSize())) {
            throw new IllegalArgumentException("Invalid size value. Allowed values are B, M, S.");
        }

        // Validar coordenadas
        if (order.getCoordinates() == null) {
            throw new IllegalArgumentException("Coordinates are required.");
        }

        order.setStatus("PENDING");
        order.setAssignedLogisticsCenter(null);  // En el POST, el centro logístico es null inicialmente

        // Guardar el pedido en la base de datos
        orderRepository.save(order);

        // Crear la respuesta para POST (con status y message)
        return new OrderResponse(order, "Order created successfully in PENDING status.");
    }

    // Obtener todos los pedidos
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();  // Obtiene todos los pedidos
        // Convertir cada Order a OrderResponse sin "message" ni "status"
        return orders.stream()
                     .map(order -> new OrderResponse(order))
                     .collect(Collectors.toList());
    }

    // Método para calcular la distancia entre dos coordenadas (fórmula de Haversine)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Devuelve la distancia en kilómetros
    }

    // Método para asignar centros logísticos a pedidos pendientes
    public String assignCentersToOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus("PENDING");
        List<Center> centers = centerService.getAllCenters();
        StringBuilder result = new StringBuilder();

        for (Order order : pendingOrders) {
            boolean assigned = false;

            for (Center center : centers) {
                // ✅ Evitar error de coordenadas null antes de asignar
                if (order.getCoordinates() == null || center.getCoordinates() == null) {
                    continue;
                }

                if (isCenterSuitableForOrder(center, order)) {
                    double distance = calculateDistance(
                            order.getCoordinates().getLatitude(), order.getCoordinates().getLongitude(),
                            center.getCoordinates().getLatitude(), center.getCoordinates().getLongitude());

                    // Asignar el centro logístico
                    order.setAssignedLogisticsCenter(center.getName());
                    order.setStatus("ASSIGNED");

                    // Asegurar que currentLoad no sea null
                    if (center.getCurrentLoad() == null) {
                        center.setCurrentLoad(0);
                    }

                    // Aumentamos la carga actual del centro
                    center.setCurrentLoad(center.getCurrentLoad() + 1);

                    // Actualizar el centro en la base de datos
                    centerService.updateCenter(center.getId(), center);

                    // Guardar el pedido actualizado
                    orderRepository.save(order);

                    // Construir la respuesta JSON
                    result.append("{\"orderId\": ").append(order.getId())
                        .append(", \"assignedLogisticsCenter\": \"").append(center.getName())
                        .append("\", \"distance\": ").append(distance)
                        .append(", \"status\": \"ASSIGNED\"},");

                    assigned = true;
                    break; // Sale del loop si se asignó un centro
                }
            }

            // Si no se asignó el pedido, mantenemos el estado PENDING
            if (!assigned) {
                result.append("{\"orderId\": ").append(order.getId())
                    .append(", \"assignedLogisticsCenter\": null")
                    .append(", \"status\": \"PENDING\", ")
                    .append("\"message\": \"All centers are at maximum capacity.\"},");
            }
        }

        // Eliminar la última coma para evitar JSON inválido
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return "{\"processed-orders\": [" + result.toString() + "]}";
    }

    // Verificar si un centro es adecuado para un pedido
    private boolean isCenterSuitableForOrder(Center center, Order order) {
        return center.getCapacity().contains(order.getSize()) &&
               center.getCurrentLoad() < center.getMaxCapacity() &&
               center.getStatus().equals("AVAILABLE");
    }
}
