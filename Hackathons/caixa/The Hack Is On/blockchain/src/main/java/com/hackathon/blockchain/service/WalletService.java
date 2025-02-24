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

    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, MarketDataService marketDataService, SmartContractService smartContractService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.marketDataService = marketDataService;
        this.smartContractService = smartContractService;
    }

    // **Método para generar claves RSA para la billetera**
    @Transactional
    public Wallet generateKeysForWallet(Long userId) throws Exception {
        Optional<Wallet> walletOpt = walletRepository.findByUserId(userId);
        if (walletOpt.isEmpty()) {
            throw new RuntimeException("Wallet not found for user with ID: " + userId);
        }
        
        Wallet wallet = walletOpt.get();
        
        // Generar claves RSA
        wallet.generateKeys();

        // Guardar billetera con las claves generadas
        walletRepository.save(wallet);

        return wallet;
    }

    // **Obtener billetera por ID de usuario**
    public Optional<Wallet> getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    // **Obtener billetera por dirección**
    public Optional<Wallet> getWalletByAddress(String address) {
        return walletRepository.findByAddress(address);
    }

    // **Compra de activo**
    @Transactional
    public String buyAsset(Long userId, String symbol, double quantity) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
        Optional<Wallet> liquidityWalletOpt = walletRepository.findByAddress("LP-" + symbol);
        Optional<Wallet> usdtLiquidityWalletOpt = walletRepository.findByAddress("LP-USDT");

        if (optionalWallet.isEmpty()) return "❌ Wallet not found!";
        if (liquidityWalletOpt.isEmpty()) return "❌ Liquidity pool for " + symbol + " not found!";
        if (usdtLiquidityWalletOpt.isEmpty()) return "❌ Liquidity pool for USDT not found!";

        Wallet userWallet = optionalWallet.get();
        Wallet liquidityWallet = liquidityWalletOpt.get();
        Wallet usdtLiquidityWallet = usdtLiquidityWalletOpt.get();

        // Verificar si el activo tiene un contrato inteligente y si cumple con las condiciones
        if (smartContractService.isTransactionBlocked(symbol, "BUY")) {
            return "❌ Transaction blocked by smart contract conditions for " + symbol;
        }

        double price = marketDataService.fetchLivePriceForAsset(symbol);
        double totalCost = quantity * price;

        if (symbol.equals("USDT")) {
            if (userWallet.getBalance() < totalCost) return "❌ Insufficient fiat balance to buy USDT!";
            userWallet.setBalance(userWallet.getBalance() - totalCost);
            updateWalletAssets(userWallet, "USDT", quantity);
            updateWalletAssets(usdtLiquidityWallet, "USDT", -quantity);
        } else {
            Optional<Asset> usdtAssetOpt = userWallet.getAssets().stream()
                    .filter(a -> a.getSymbol().equals("USDT"))
                    .findFirst();
            if (usdtAssetOpt.isEmpty() || usdtAssetOpt.get().getQuantity() < totalCost) {
                return "❌ Insufficient USDT balance! You must buy USDT first.";
            }
            updateWalletAssets(userWallet, "USDT", -totalCost);
            updateWalletAssets(usdtLiquidityWallet, "USDT", totalCost);
            updateWalletAssets(userWallet, symbol, quantity);
            updateWalletAssets(liquidityWallet, symbol, -quantity);
        }
        walletRepository.saveAll(List.of(userWallet, liquidityWallet, usdtLiquidityWallet));
        recordTransaction(liquidityWallet, userWallet, symbol, quantity, price, "BUY");
        return "✅ Asset purchased successfully!";
    }

    // **Venta de activo**
    @Transactional
    public String sellAsset(Long userId, String symbol, double quantity) {
        Optional<Wallet> optionalWallet = walletRepository.findByUserId(userId);
        Optional<Wallet> liquidityWalletOpt = walletRepository.findByAddress("LP-" + symbol);

        if (optionalWallet.isEmpty()) return "❌ Wallet not found!";
        if (liquidityWalletOpt.isEmpty()) return "❌ Liquidity pool for " + symbol + " not found!";

        Wallet userWallet = optionalWallet.get();
        Wallet liquidityWallet = liquidityWalletOpt.get();

        // Verificar si el activo tiene un contrato inteligente y si cumple con las condiciones
        if (smartContractService.isTransactionBlocked(symbol, "SELL")) {
            return "❌ Transaction blocked by smart contract conditions for " + symbol;
        }

        double price = marketDataService.fetchLivePriceForAsset(symbol);
        double totalRevenue = quantity * price;

        Optional<Asset> existingAsset = userWallet.getAssets().stream()
                .filter(a -> a.getSymbol().equals(symbol))
                .findFirst();

        if (existingAsset.isEmpty() || existingAsset.get().getQuantity() < quantity) {
            return "❌ Not enough assets to sell!";
        }
        updateWalletAssets(userWallet, symbol, -quantity);
        updateWalletAssets(liquidityWallet, symbol, quantity);
        updateWalletAssets(userWallet, "USDT", totalRevenue);
        walletRepository.saveAll(List.of(userWallet, liquidityWallet));
        recordTransaction(userWallet, liquidityWallet, symbol, quantity, price, "SELL");
        return "✅ Asset sold successfully!";
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

    // **Grabar transacciones**
    private void recordTransaction(Wallet sender, Wallet receiver, String assetSymbol, double quantity, double price, String type) {
        Transaction transaction = new Transaction(
            UUID.randomUUID().toString(),  // ID generado automáticamente
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
