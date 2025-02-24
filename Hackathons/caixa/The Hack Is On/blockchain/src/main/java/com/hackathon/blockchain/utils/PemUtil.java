package com.hackathon.blockchain.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;  // Esta importación es la correcta
import java.util.Base64;

public class PemUtil {

    // Método para convertir una clave privada PEM en un objeto PrivateKey
    public static PrivateKey getPrivateKeyFromPem(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Limpieza de las etiquetas del formato PEM
        String privateKeyPEM = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                                  .replace("-----END PRIVATE KEY-----", "")
                                  .replaceAll("\\s+", "");  // Elimina cualquier espacio adicional
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  // Puedes cambiar "RSA" por otro algoritmo si es necesario
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }

    // Método para convertir una clave pública PEM en un objeto PublicKey
    public static PublicKey getPublicKeyFromPem(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Limpieza de las etiquetas del formato PEM
        String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                                 .replace("-----END PUBLIC KEY-----", "")
                                 .replaceAll("\\s+", "");  // Elimina cualquier espacio adicional
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");  // Puedes cambiar "RSA" por otro algoritmo si es necesario
        return keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
    }

    // Método para convertir un objeto PrivateKey a su formato PEM
    public static String toPEMFormat(PrivateKey privateKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");
        pem.append(encoder.encodeToString(privateKey.getEncoded()).replaceAll("(.{64})", "$1\n"));  // Formateo con líneas de 64 caracteres
        pem.append("\n-----END PRIVATE KEY-----");
        return pem.toString();
    }

    // Método para convertir un objeto PublicKey a su formato PEM
    public static String toPEMFormat(PublicKey publicKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PUBLIC KEY-----\n");
        pem.append(encoder.encodeToString(publicKey.getEncoded()).replaceAll("(.{64})", "$1\n"));  // Formateo con líneas de 64 caracteres
        pem.append("\n-----END PUBLIC KEY-----");
        return pem.toString();
    }

    // Método para verificar si una clave PEM está correctamente formada
    public static boolean isValidPemFormat(String pem) {
        return pem != null && pem.startsWith("-----BEGIN") && pem.endsWith("-----END");
    }
}
