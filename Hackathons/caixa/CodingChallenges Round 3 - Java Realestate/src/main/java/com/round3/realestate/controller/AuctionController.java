package com.round3.realestate.controller;

import com.round3.realestate.entity.Auction;
import com.round3.realestate.entity.Property;
import com.round3.realestate.payload.AuctionRequest;
import com.round3.realestate.repository.AuctionRepository;
import com.round3.realestate.repository.PropertyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@RestController
@RequestMapping("/api/auction")
public class AuctionController {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    // Método para crear una subasta
    @PostMapping("/create")
    public ResponseEntity<?> createAuction(@RequestBody AuctionRequest request) {
        // Buscar la propiedad por ID
        Optional<Property> propertyOpt = propertyRepository.findById(request.getPropertyId());
        if (propertyOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"error\": \"Property not found.\"}");
        }

        Property property = propertyOpt.get();

        // Verificar que la propiedad esté disponible
        if (!"Available".equalsIgnoreCase(property.getStatus())) {  // Asumí que el nombre correcto es 'status' en lugar de 'availability'
            return ResponseEntity.badRequest().body("{\"success\": false, \"error\": \"Property is not available.\"}");
        }

        // Crear una nueva subasta
        Auction auction = new Auction();
        auction.setProperty(property);  // Asignar la propiedad encontrada a la subasta
        auction.setStartTime(request.getStartTime());  // Establecer el inicio de la subasta
        auction.setEndTime(request.getEndTime());  // Establecer el fin de la subasta
        auction.setMinIncrement(request.getMinIncrement());  // Incremento mínimo
        auction.setStartingPrice(request.getStartingPrice());  // Precio inicial
        auction.setCurrentHighestBid(request.getStartingPrice());  // La primera oferta es el precio inicial
        auction.setStatus("open");  // Establecer el estado de la subasta a "open"

        // Guardar la subasta en la base de datos
        auctionRepository.save(auction);

        // Responder con un mensaje de éxito
        return ResponseEntity.ok("{\"success\": true, \"message\": \"Auction created successfully.\", \"auctionId\": " + auction.getId() + "}");
    }

    // Método para obtener una subasta por ID
    @GetMapping("/{auctionId}")
    public ResponseEntity<?> getAuction(@PathVariable Long auctionId) {
        // Buscar la subasta por ID
        Optional<Auction> auctionOpt = auctionRepository.findById(auctionId);
        if (auctionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"error\": \"Auction not found.\"}");
        }
        
        // Devolver la subasta encontrada
        return ResponseEntity.ok(auctionOpt.get());
    }
}
