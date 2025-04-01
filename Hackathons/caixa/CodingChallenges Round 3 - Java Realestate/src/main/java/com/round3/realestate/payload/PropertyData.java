package com.round3.realestate.payload;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PropertyData {

    private String fullTitle;
    private String rooms;
    private String size;
    
    @JsonIgnore // Se usa solo para la BD, no se devuelve en la respuesta JSON
    private BigDecimal price; 
    
    private String location;
    private String type;
    private String url;

    public PropertyData(String fullTitle, String rooms, String size, BigDecimal price, String location, String type, String url) {
        this.fullTitle = fullTitle;
        this.rooms = rooms;
        this.size = size;
        this.price = price;
        this.location = location;
        this.type = type;
        this.url = url;
    }

    // ✅ Asegurar que el JSON devuelve "price" en el formato correcto sin "rawPrice"
    @JsonGetter("price")
    public String getFormattedPrice() {
        DecimalFormat format = new DecimalFormat("#,###");
        return format.format(price).replace(",", ".");
    }

    // ✅ Se usa internamente en Hibernate pero no en la respuesta JSON
    public BigDecimal getPrice() { 
        return price; 
    } 
    
    public void setPrice(BigDecimal price) { 
        this.price = price; 
    }

    // Getters y setters
    public String getFullTitle() { return fullTitle; }
    public void setFullTitle(String fullTitle) { this.fullTitle = fullTitle; }

    public String getRooms() { return rooms; }
    public void setRooms(String rooms) { this.rooms = rooms; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
