package com.hackathon.inditex.Services;

import com.hackathon.inditex.Entities.Center;
import com.hackathon.inditex.Repositories.CenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import com.hackathon.inditex.DTO.ApiResponse;


@Service
public class CenterService {

    private static final Logger logger = LoggerFactory.getLogger(CenterService.class);

    @Autowired
    private CenterRepository centerRepository;

    private static final String[] VALID_CAPACITIES = {"B", "M", "S", "BS", "MS", "BMS"};

    private boolean isValidCapacity(String capacity) {
        for (String validCapacity : VALID_CAPACITIES) {
            if (validCapacity.equals(capacity)) {
                return true;
            }
        }
        return false;
    }
    // Crear un nuevo centro logístico
    public ResponseEntity<Map<String, String>> createCenter(Center center) {
        logger.info("Creating new center: {}", center.getName());

        Map<String, String> response = new HashMap<>();

        if (center.getMaxCapacity() == null) {
            response.put("message", "Max capacity cannot be null.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (center.getCurrentLoad() == null) {
            center.setCurrentLoad(0);
        }

        if (center.getCurrentLoad() > center.getMaxCapacity()) {
            response.put("message", "Current load cannot exceed max capacity.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (center.getCapacity() == null || !isValidCapacity(center.getCapacity())) {
            response.put("message", "Invalid capacity value. Allowed values are B, M, S, BS, MS, BMS.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (centerRepository.existsByCoordinates_LatitudeAndCoordinates_Longitude(
                center.getCoordinates().getLatitude(), center.getCoordinates().getLongitude())) {
            response.put("message", "There is already a logistics center in that position.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        centerRepository.save(center);
        response.put("message", "Logistics center created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Actualizar los detalles de un centro existente
    public ApiResponse updateCenter(Long id, Center center) {
        logger.info("Starting update for center ID: {}", id);

        // Buscar el centro en la base de datos
        Center existingCenter = centerRepository.findById(id).orElse(null);
        if (existingCenter == null) {
            logger.warn("Center with ID: {} not found", id);
            return new ApiResponse("Logistics center not found."); // Solo un mensaje de error
        }

        // Verificar si el currentLoad excede maxCapacity
        if (center.getCurrentLoad() != null) {
            // Asegurarnos de que maxCapacity se trate como Long
            Long maxCapacityToCheck = (center.getMaxCapacity() != null) ? Long.valueOf(center.getMaxCapacity()) : Long.valueOf(existingCenter.getMaxCapacity());

            if (maxCapacityToCheck != null && center.getCurrentLoad() > maxCapacityToCheck) {
                logger.warn("Validation failed: currentLoad {} exceeds maxCapacity {}", center.getCurrentLoad(), maxCapacityToCheck);
                return new ApiResponse("Current load cannot exceed max capacity.");  // Solo el mensaje de error
            }
        }

        // Actualizar los campos solo si no son nulos
        if (center.getName() != null) existingCenter.setName(center.getName());
        if (center.getCapacity() != null) existingCenter.setCapacity(center.getCapacity());
        if (center.getStatus() != null) existingCenter.setStatus(center.getStatus());
        if (center.getMaxCapacity() != null) existingCenter.setMaxCapacity(center.getMaxCapacity());
        if (center.getCurrentLoad() != null) existingCenter.setCurrentLoad(center.getCurrentLoad());

        // Guardar la actualización
        centerRepository.save(existingCenter);
        logger.info("Center updated successfully: {}", existingCenter.getName());
        return new ApiResponse("Logistics center updated successfully."); // Solo el mensaje de éxito
    }


    // Eliminar un centro logístico
    public String deleteCenter(Long id) {
        logger.info("Starting delete for center ID: {}", id);

        if (!centerRepository.existsById(id)) {
            logger.warn("Center with ID: {} not found", id);
            return "Logistics center not found."; // Solo el mensaje
        }

        centerRepository.deleteById(id);
        logger.info("Logistics center with ID: {} deleted successfully.", id);
        return "Logistics center deleted successfully."; // Solo el mensaje
    }


    // Obtener todos los centros logísticos
    public List<Center> getAllCenters() {
        logger.info("Fetching all logistics centers.");
        return centerRepository.findAll();
    }


    // Obtener un centro por su id
    public Center getCenterById(Long id) {
        logger.info("Fetching center by ID: {}", id);
        return centerRepository.findById(id).orElse(null);
    }
}
