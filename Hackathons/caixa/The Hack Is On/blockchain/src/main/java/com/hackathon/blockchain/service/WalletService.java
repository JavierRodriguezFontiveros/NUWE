package com.hackathon.blockchain.service;

import com.hackathon.blockchain.model.*;
import com.hackathon.blockchain.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final MarketDataService marketDataService;
    private final SmartContractService smartContractService;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, MarketDataService marketDataService, SmartContractService smartContractService, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.marketDataService = marketDataService;
        this.smartContractService = smartContractService;
        this.userRepository = userRepository;
    }

    // **Método para crear una billetera**
    @Transactional
    public Wallet createWallet(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Crear una nueva billetera con saldo inicial de 100,000$
        Wallet wallet = new Wallet("address_" + UUID.randomUUID().toString(), 100000.0, user);
        wallet.setUser(user);
        walletRepository.save(wallet);

        return wallet;
    }

    // **Método para generar claves RSA para la billetera**
    @Transactional
    public Wallet generateKeysForWallet(Long userId) {
        Optional<Wallet> walletOpt = walletRepository.findByUser_Id(userId);
        if (walletOpt.isEmpty()) {
            log.error("Wallet not found for user with ID: {}", userId);
            throw new RuntimeException("Wallet not found for user with ID: " + userId);
        }

        Wallet wallet = walletOpt.get();

        try {
            log.info("Generando claves RSA para la billetera del usuario: {}", userId);
            wallet.generateKeys();
            walletRepository.save(wallet);
            log.info("Claves RSA generadas y billetera actualizada para el usuario: {}", userId);
        } catch (Exception e) {
            log.error("Error generando claves RSA para el usuario {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Error al generar claves RSA para la billetera", e);
        }

        return wallet;
    }


    // **Método para vender un activo**
    @Transactional
    public String sellAsset(Long userId, String symbol, double quantity) {
        Optional<Wallet> optionalWallet = walletRepository.findByUser_Id(userId);
        Optional<Wallet> liquidityWalletOpt = walletRepository.findByAddress("LP-" + symbol);
        Optional<Wallet> usdtLiquidityWalletOpt = walletRepository.findByAddress("LP-USDT");

        if (optionalWallet.isEmpty()) return "❌ Wallet not found!";
        if (liquidityWalletOpt.isEmpty()) return "❌ Liquidity pool for " + symbol + " not found!";
        if (usdtLiquidityWalletOpt.isEmpty()) return "❌ Liquidity pool for USDT not found!";

        Wallet userWallet = optionalWallet.get();
        Wallet liquidityWallet = liquidityWalletOpt.get();
        Wallet usdtLiquidityWallet = usdtLiquidityWalletOpt.get();

        // Verificar si el activo tiene un contrato inteligente y si cumple con las condiciones
        if (smartContractService.isTransactionBlocked(symbol, "SELL")) {
            return "❌ Transaction blocked by smart contract conditions for " + symbol;
        }

        double price = marketDataService.fetchLivePriceForAsset(symbol);
        double totalCost = quantity * price;

        Optional<Asset> assetOpt = userWallet.getAssets().stream()
                .filter(a -> a.getSymbol().equals(symbol))
                .findFirst();

        if (assetOpt.isEmpty() || assetOpt.get().getQuantity() < quantity) {
            return "❌ Insufficient asset balance!";
        }

        updateWalletAssets(userWallet, symbol, -quantity);
        updateWalletAssets(liquidityWallet, symbol, quantity);
        updateWalletAssets(usdtLiquidityWallet, "USDT", totalCost);

        walletRepository.saveAll(List.of(userWallet, liquidityWallet, usdtLiquidityWallet));

        recordTransaction(userWallet, liquidityWallet, symbol, quantity, price, "SELL");
        return "✅ Asset sold successfully!";
    }

    // **Método para obtener la billetera de un usuario**
    public Wallet getWalletByUserId(Long userId) {
        return walletRepository.findByUser_Id(userId).orElseThrow(() -> new RuntimeException("Wallet not found for user with ID: " + userId));
    }

    // **Método para obtener o crear la billetera**
    @Transactional
    public Wallet getOrCreateWalletForUser(Long userId) {
        Optional<Wallet> walletOptional = walletRepository.findByUser_Id(userId);
        if (walletOptional.isPresent()) {
            return walletOptional.get();
        } else {
            // Si no existe la billetera, la crea
            return createWallet(userId);
        }
    }

    // **Método para obtener los detalles de la billetera**
    public WalletDetails getWalletDetails(Long userId) {
        Wallet wallet = getWalletByUserId(userId);  // Obtener la billetera

        // Calcular el saldo total en efectivo y los detalles de los activos
        double totalBalance = wallet.getBalance();  // El saldo en efectivo
        Map<String, Double> assetDetails = new HashMap<>();
        
        // Calcular el valor de cada activo y llenar el mapa de activos
        for (Asset asset : wallet.getAssets()) {
            double assetValue = asset.getQuantity() * marketDataService.fetchLivePriceForAsset(asset.getSymbol());
            assetDetails.put(asset.getSymbol(), assetValue);
            totalBalance += assetValue;  // Sumar al valor neto
        }

        // Crear y retornar un nuevo objeto WalletDetails
        WalletDetails walletDetails = new WalletDetails(wallet.getBalance(), totalBalance, assetDetails);
        walletDetails.setWalletAddress(wallet.getAddress());  // Establecer la dirección de la billetera

        return walletDetails;  // Retornar el objeto con todos los detalles
    }

    // **Método para comprar un activo**
    @Transactional
    public String buyAsset(Long userId, String symbol, double quantity) {
        Optional<Wallet> walletOpt = walletRepository.findByUser_Id(userId);
        if (walletOpt.isEmpty()) return "❌ Wallet not found!";

        Wallet wallet = walletOpt.get();

        // Verificar si el contrato inteligente permite la compra
        if (smartContractService.isTransactionBlocked(symbol, "BUY")) {
            return "❌ Transaction blocked by smart contract conditions for " + symbol;
        }

        double price = marketDataService.fetchLivePriceForAsset(symbol);
        double totalCost = quantity * price;

        if (wallet.getBalance() < totalCost) {
            return "❌ Insufficient balance!";
        }

        // Actualizar el saldo de la billetera
        updateWalletAssets(wallet, symbol, quantity);
        wallet.setBalance(wallet.getBalance() - totalCost);
        walletRepository.save(wallet);

        recordTransaction(wallet, wallet, symbol, quantity, price, "BUY");
        return "✅ Asset bought successfully!";
    }

    // **Método para obtener el historial de transacciones**
    public TransactionHistory getTransactionHistory(Long userId) {
        List<Transaction> transactions = transactionRepository.findBySenderWallet_User_IdOrDestinationWallet_User_Id(userId, userId);
        return new TransactionHistory(transactions, transactions); // Ajuste para el constructor
    }

    // **Actualizar activos en la billetera**
    private void updateWalletAssets(Wallet wallet, String assetSymbol, double amount) {
        Optional<Asset> assetOpt = wallet.getAssets().stream()
                .filter(asset -> asset.getSymbol().equalsIgnoreCase(assetSymbol))
                .findFirst();
        if (assetOpt.isPresent()) {
            Asset asset = assetOpt.get();
            asset.setQuantity(asset.getQuantity() + amount);
            if (asset.getQuantity() <= 0) {
                wallet.getAssets().remove(asset);
            }
        } else if (amount > 0) {
            Asset newAsset = new Asset();
            newAsset.setSymbol(assetSymbol);
            newAsset.setQuantity(amount);
            newAsset.setWallet(wallet);
            wallet.getAssets().add(newAsset);
        }
    }

    // **Método para grabar las transacciones**
    private void recordTransaction(Wallet sender, Wallet receiver, String assetSymbol, double quantity, double price, String type) {
        Transaction transaction = new Transaction(
            UUID.randomUUID().toString(),
            sender.getAddress(),
            receiver.getAddress(),
            sender,
            receiver,
            assetSymbol,
            quantity,
            price,
            type,
            new Date()
        );
        transactionRepository.save(transaction);
    }

    // **Método para calcular y aplicar una comisión a la transacción**
    @Transactional
    public Double transferFee(Transaction transaction, Double feePercentage) {
        // Verificar que el porcentaje sea válido
        if (feePercentage < 0 || feePercentage > 100) {
            throw new IllegalArgumentException("Invalid fee percentage");
        }

        // Calcular la comisión
        Double fee = transaction.getAmount() * feePercentage / 100;

        // Actualizar el saldo de la billetera (esto puede variar dependiendo de tu lógica de negocio)
        Wallet sender = transaction.getSender();
        sender.setBalance(sender.getBalance() - fee);

        // Guardar la billetera después de actualizar el saldo
        walletRepository.save(sender);

        // Retornar el valor de la comisión calculada
        return fee;
    }

    // **Actualizar balances programados**
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void updateWalletBalancesScheduled() {
        log.info("🔄 Updating wallet net worths based on live market prices...");
        List<Wallet> wallets = walletRepository.findAll();
        for (Wallet wallet : wallets) {
            double totalValue = wallet.getBalance();
            for (Asset asset : wallet.getAssets()) {
                double marketPrice = marketDataService.fetchLivePriceForAsset(asset.getSymbol());
                totalValue += asset.getQuantity() * marketPrice;
            }
            wallet.setNetWorth(totalValue);
            walletRepository.save(wallet);
        }
        log.info("✅ All wallet net worths updated successfully!");
    }
}
