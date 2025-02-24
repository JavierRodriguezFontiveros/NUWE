package com.hackathon.blockchain.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MarketDataService {

    // Método para obtener el precio en vivo de un activo (por ejemplo, una criptomoneda)
    public double fetchLivePriceForAsset(String assetName) {
        // Lógica para obtener el precio en vivo del activo. En un caso real, aquí harías una llamada a una API externa.
        switch (assetName.toUpperCase()) {
            case "BTC":
                return 35000.0;  // Ejemplo de precio en vivo de Bitcoin
            case "ETH":
                return 2500.0;   // Ejemplo de precio en vivo de Ethereum
            case "USDT":
                return 1.0;      // Precio de USDT (Stablecoin)
            case "NCOIN":
                return 10.0;     // Ejemplo de precio de NCOIN
            case "CCOIN":
                return 10.0;     // Ejemplo de precio de CCOIN
            default:
                return -1.0;     // Retorna -1.0 si no se encuentra el activo
        }
    }

    // Método para obtener precios del mercado en vivo de varios activos
    public Map<String, Double> getLiveMarketPrices() {
        Map<String, Double> prices = new HashMap<>();
        
        // Agregamos los precios de los activos más comunes
        prices.put("BTC", fetchLivePriceForAsset("BTC"));
        prices.put("ETH", fetchLivePriceForAsset("ETH"));
        prices.put("USDT", fetchLivePriceForAsset("USDT"));
        prices.put("NCOIN", fetchLivePriceForAsset("NCOIN"));
        prices.put("CCOIN", fetchLivePriceForAsset("CCOIN"));

        return prices;
    }
}
