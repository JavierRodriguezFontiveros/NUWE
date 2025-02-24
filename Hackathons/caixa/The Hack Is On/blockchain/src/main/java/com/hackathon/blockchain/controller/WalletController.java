package com.hackathon.blockchain.controller;

import com.hackathon.blockchain.model.Wallet;
import com.hackathon.blockchain.model.Transaction;
import com.hackathon.blockchain.service.WalletService;
import com.hackathon.blockchain.service.MarketDataService;
import com.hackathon.blockchain.response.ResponseMessage;
import com.hackathon.blockchain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;  // Servicio para obtener los datos del usuario

    @Autowired
    private MarketDataService marketDataService;  // Servicio para obtener precios de mercado

    // **1. POST /wallet/crear**: Crear una billetera con saldo inicial de 100,000$
    @Secured("ROLE_USER")  // Asegura que el usuario esté autenticado
    @PostMapping("/crear")
    public ResponseMessage createWallet(@RequestParam Long userId) {
        Wallet newWallet = walletService.createWallet(userId);
        return new ResponseMessage("✅ Wallet successfully created! Address: " + newWallet.getAddress());
    }

    // **2. POST /wallet/generar-claves**: Generar un par de claves RSA para la billetera
    @Secured("ROLE_USER")  // Asegura que el usuario esté autenticado
    @PostMapping("/generar-claves")
    public ResponseMessage generateKeys(@RequestParam Long walletId) throws Exception {
        Wallet wallet = walletService.getWalletById(walletId);
        
        if (wallet == null) {
            throw new RuntimeException("Wallet not found!");
        }

        // Generar un par de claves RSA para la billetera
        walletService.generateKeysForWallet(wallet);

        return new ResponseMessage("✅ Keys generated/retrieved successfully for wallet id: " + walletId);
    }

    // **3. GET /market/prices**: Obtener todos los precios del mercado
    @GetMapping("/market/prices")
    public Map<String, Double> getMarketPrices() {
        return marketDataService.getLiveMarketPrices();
    }

    // **4. GET /market/price/{symbol}**: Obtener el precio de un activo específico
    @GetMapping("/market/price/{symbol}")
    public String getAssetPrice(@PathVariable String symbol) {
        double price = marketDataService.getLivePriceForAsset(symbol);
        if (price == -1.0) {
            return "{\"message\": \"Price for " + symbol + " not found\"}";
        }
        return "{\"message\": \"Current price of " + symbol + ": $" + price + "\"}";
    }

    // **5. Consultar saldo de la billetera**
    @GetMapping("/saldo")
    public WalletDetails getWalletDetails(@RequestParam Long userId) {
        return walletService.getWalletDetails(userId);
    }

    // **6. Comprar un activo**
    @PostMapping("/comprar")
    public ResponseMessage buyAsset(@RequestParam Long userId, @RequestBody BuyRequest buyRequest) {
        String response = walletService.buyAsset(userId, buyRequest.getSymbol(), buyRequest.getQuantity());
        return new ResponseMessage(response);
    }

    // **7. Vender un activo**
    @PostMapping("/vender")
    public ResponseMessage sellAsset(@RequestParam Long userId, @RequestBody SellRequest sellRequest) {
        String response = walletService.sellAsset(userId, sellRequest.getSymbol(), sellRequest.getQuantity());
        return new ResponseMessage(response);
    }

    // **8. Ver el historial de transacciones**
    @GetMapping("/transacciones")
    public TransactionHistory getTransactionHistory(@RequestParam Long userId) {
        return walletService.getTransactionHistory(userId);
    }
}
