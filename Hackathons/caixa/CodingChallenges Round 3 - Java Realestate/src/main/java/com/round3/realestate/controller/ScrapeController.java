package com.round3.realestate.controller;

import com.round3.realestate.payload.ScrapeRequest;
import com.round3.realestate.service.ScrapingService;
import com.round3.realestate.payload.ScrapeResponse; // Nueva clase de respuesta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scrape")
public class ScrapeController {

    @Autowired
    private ScrapingService scrapingService;

    @PostMapping
    public ScrapeResponse scrapeProperty(@RequestBody ScrapeRequest scrapeRequest) {
        // Obtener los datos de la propiedad a partir de la URL
        return scrapingService.scrapeProperty(scrapeRequest.getUrl(), scrapeRequest.isStore());
    }
}
