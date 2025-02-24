package com.hackathon.blockchain.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WalletsUtil {

    private static final String ALGORITHM = "RSA";

    // Método para generar una nueva wallet (par de claves pública/privada)
    public static KeyPair generateWallet() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(2048); // Tamaño de la clave (2048 bits recomendado para RSA)
        return keyPairGenerator.generateKeyPair();
    }

    // Método para convertir una clave pública a formato PEM
    public static String publicKeyToPEM(PublicKey publicKey) {
        return PemUtil.toPEMFormat(publicKey);
    }

    // Método para convertir una clave privada a formato PEM
    public static String privateKeyToPEM(PrivateKey privateKey) {
        return PemUtil.toPEMFormat(privateKey);
    }

    // Método para generar una wallet como cadenas PEM (clave privada y pública)
    public static String[] generateWalletPEM() throws NoSuchAlgorithmException {
        KeyPair keyPair = generateWallet();
        String privateKeyPEM = privateKeyToPEM(keyPair.getPrivate());
        String publicKeyPEM = publicKeyToPEM(keyPair.getPublic());
        return new String[] { privateKeyPEM, publicKeyPEM };
    }

    // Método para recuperar una clave privada a partir de un formato PEM
    public static PrivateKey getPrivateKeyFromPEM(String privateKeyPEM) throws Exception {
        return PemUtil.getPrivateKeyFromPem(privateKeyPEM);
    }

    // Método para recuperar una clave pública a partir de un formato PEM
    public static PublicKey getPublicKeyFromPEM(String publicKeyPEM) throws Exception {
        return PemUtil.getPublicKeyFromPem(publicKeyPEM);
    }
}
