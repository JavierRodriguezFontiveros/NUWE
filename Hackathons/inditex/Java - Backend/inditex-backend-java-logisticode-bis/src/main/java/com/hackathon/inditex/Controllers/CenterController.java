package com.hackathon.inditex.Controllers;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Services.CenterService;
import com.hackathon.inditex.DTO.ApiResponse; // Importamos ApiResponse desde DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/centers")
public class CenterController {

    @Autowired
    private CenterService centerService;

    private static final Logger logger = LoggerFactory.getLogger(CenterController.class);

    @PostMapping
    public ResponseEntity<Map<String, String>> createCenter(@RequestBody Center center) {
        logger.info("Received request to create a new center: {}", center);
        return centerService.createCenter(center);
    }

    // Obtener lista de centros logísticos
    @GetMapping
    public ResponseEntity<List<Center>> getAllCenters() {
        List<Center> centers = centerService.getAllCenters();

        if (centers.isEmpty()) {
            logger.info("No logistics centers found.");
            return ResponseEntity.noContent().build(); // Devuelve 204 si la lista está vacía
        }

        logger.info("Returning {} logistics centers.", centers.size());
        return ResponseEntity.ok(centers);
    }

    // Actualizar centro logístico (usando PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCenter(@PathVariable Long id, @RequestBody Center center) {
        logger.info("Received request to update center with ID: {}, Data: {}", id, center);

        ApiResponse response = centerService.updateCenter(id, center);

        // Si el mensaje contiene el error, devuelve 400 Bad Request
        if (response.getMessage().contains("cannot exceed max capacity")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Respuesta satisfactoria
        return ResponseEntity.ok(response);
    }






    // Eliminar centro logístico
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCenter(@PathVariable Long id) {
        logger.info("Received request to delete center with ID: {}", id);

        String responseMessage = centerService.deleteCenter(id);
        logger.info("Response from service: {}", responseMessage);

        if (responseMessage.contains("not found")) {
            return ResponseEntity.status(404).body(Map.of("message", responseMessage)); // 404 si no se encuentra el centro
        }
        return ResponseEntity.ok(Map.of("message", responseMessage)); // 200 para eliminación exitosa
    }
}
