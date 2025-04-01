package com.round3.realestate.service;

import com.round3.realestate.entity.Property;
import com.round3.realestate.repository.PropertyRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.round3.realestate.payload.ScrapeResponse;
import com.round3.realestate.payload.PropertyData;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ScrapingService {

    @Autowired
    private PropertyRepository propertyRepository;

    public ScrapeResponse scrapeProperty(String url, boolean store) {
        try {
            // Realizar la solicitud con cabeceras necesarias
            Connection.Response response = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .header("Accept-Language", "en-US,en;q=0.9,es;q=0.8")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("Referer", "https://www.google.com/")  // Referer para evitar bloqueos
                    .execute();

            // Verificar código de estado
            if (response.statusCode() == 200) {
                Document doc = response.parse();

                // Título completo (no se almacena, solo se usa para extraer el tipo)
                String title = doc.select("#main > div > main > section.detail-info.ide-box-detail-first-picture.ide-box-detail--reset.overlay-box > section > div.main-info__title > h1 > span").text();
                String fullTitle = title.trim();

                // Extraer el tipo de propiedad (primer palabra del título)
                String propertyType = fullTitle.split(" ")[0];  // Asumimos que la primera palabra es el tipo

                // Verificar que el tipo de propiedad no sea nulo ni vacío
                if (propertyType == null || propertyType.isEmpty()) {
                    propertyType = "Desconocido";  // Asignar un valor por defecto si no se extrae correctamente el tipo
                }

                // Ubicación
                String location = doc.select("#main > div > main > section.detail-info.ide-box-detail-first-picture.ide-box-detail--reset.overlay-box > section > div.main-info__title > span > span").text();

                // Extraer el precio en formato correcto y convertir a BigDecimal para la base de datos
                String priceString = doc.select("span.txt-bold").text().replaceAll("[^0-9]", "");
                BigDecimal price = priceString.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceString);

                // Tamaño y habitaciones
                String size = doc.select("#main > div > main > section.detail-info.ide-box-detail-first-picture.ide-box-detail--reset.overlay-box > section > div.info-features > span:nth-child(1)").text();
                String rooms = doc.select("#main > div > main > section.detail-info.ide-box-detail-first-picture.ide-box-detail--reset.overlay-box > section > div.info-features > span:nth-child(2)").text();

                // Sanitizar el tamaño para corregir "m�" a "m²"
                if (size.contains("m�")) {
                    size = size.replace("m�", "m²");  // Reemplaza el carácter corrupto por "m²"
                }

                // Crear la propiedad (se guarda en la base de datos)
                Property property = new Property(location, price, size, rooms, "Available");
                property.setType(propertyType);  // Establecer el tipo de propiedad en el campo 'name'

                // Si store es true, guarda la propiedad
                if (store) {
                    propertyRepository.save(property);
                }

                // Crear y devolver la respuesta esperada con PropertyData como DTO
                PropertyData propertyData = new PropertyData(fullTitle, rooms, size, price, location, propertyType, url);
                ScrapeResponse responseData = new ScrapeResponse(propertyData, store);

                return responseData;

            } else {
                throw new RuntimeException("Error al obtener los datos de la propiedad. Código de estado: " + response.statusCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al obtener los datos de la propiedad.");
        }
    }
}
