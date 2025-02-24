package com.hackathon.blockchain.service;

import com.hackathon.blockchain.model.Wallet;
import com.hackathon.blockchain.model.WalletKey;
import com.hackathon.blockchain.repository.WalletKeyRepository;
import com.hackathon.blockchain.utils.PemUtil;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

import com.hackathon.blockchain.repository.TransactionRepository;
import com.hackathon.blockchain.repository.SmartContractRepository;
import com.hackathon.blockchain.exceptions.*;

@Service
public class WalletKeyService {

    private static final String KEYS_FOLDER = "keys";
    private final WalletKeyRepository walletKeyRepository;

    public WalletKeyService(WalletKeyRepository walletKeyRepository) throws IOException {
        this.walletKeyRepository = walletKeyRepository;
        File dir = new File(KEYS_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public Optional<WalletKey> getKeysByWallet(Wallet wallet) {
        return walletKeyRepository.findByWallet(wallet);
    }

    public Optional<WalletKey> getKeysByWalletId(Long walletId) {
        return walletKeyRepository.findByWalletId(walletId);
    }

    public WalletKey generateAndStoreKeys(Wallet wallet) throws NoSuchAlgorithmException, IOException {
        if (walletKeyRepository.findByWallet(wallet).isPresent()) {
            throw new RuntimeException("Keys already generated for this wallet");
        }

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        // Corregido: se eliminó el segundo parámetro ("PUBLIC" / "PRIVATE")
        String publicKeyPEM = PemUtil.toPEMFormat(keyPair.getPublic());
        String privateKeyPEM = PemUtil.toPEMFormat(keyPair.getPrivate());

        Path privateKeyPath = Path.of(KEYS_FOLDER, "wallet_" + wallet.getId() + "_private.pem");
        Path publicKeyPath = Path.of(KEYS_FOLDER, "wallet_" + wallet.getId() + "_public.pem");

        try (FileOutputStream fos = new FileOutputStream(privateKeyPath.toFile())) {
            fos.write(privateKeyPEM.getBytes());
        }
        try (FileOutputStream fos = new FileOutputStream(publicKeyPath.toFile())) {
            fos.write(publicKeyPEM.getBytes());
        }

        WalletKey walletKey = new WalletKey();
        walletKey.setWallet(wallet);
        walletKey.setPublicKey(publicKeyPEM);  // Guardar como String en formato PEM
        walletKey.setPrivateKey(privateKeyPEM);  // Guardar como String en formato PEM

        return walletKeyRepository.save(walletKey);
    }

    public PublicKey getPublicKeyForWallet(Long walletId) throws KeyGenerationException, KeyNotFoundException {
        Optional<WalletKey> keyOpt = walletKeyRepository.findByWalletId(walletId);
        if (keyOpt.isPresent()) {
            String publicKeyPEM = keyOpt.get().getPublicKey();
            try {
                String publicKeyContent = publicKeyPEM
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replaceAll("\\s", "");
                byte[] decoded = Base64.getDecoder().decode(publicKeyContent);
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            } catch (Exception e) {
                throw new KeyGenerationException("Error generating public key", e);
            }
        }
        throw new KeyNotFoundException("Public key not found for wallet ID: " + walletId);
    }

    public PrivateKey getPrivateKeyForWallet(Long walletId) throws KeyGenerationException, KeyNotFoundException {
        Optional<WalletKey> keyOpt = walletKeyRepository.findByWalletId(walletId);
        if (keyOpt.isPresent()) {
            String privateKeyPEM = keyOpt.get().getPrivateKey();
            try {
                String privateKeyContent = privateKeyPEM
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");
                byte[] decoded = Base64.getDecoder().decode(privateKeyContent);
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePrivate(keySpec);
            } catch (Exception e) {
                throw new KeyGenerationException("Error generating private key", e);
            }
        }
        throw new KeyNotFoundException("Private key not found for wallet ID: " + walletId);
    }
}
