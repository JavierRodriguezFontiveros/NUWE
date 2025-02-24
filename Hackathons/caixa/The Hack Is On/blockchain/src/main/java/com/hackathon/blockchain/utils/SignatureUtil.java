package com.hackathon.blockchain.utils;

import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class SignatureUtil {

    // Método para generar una firma digital (ya está implementado)
    public static String getDigitalSignature(String data) {
        // Implementación de la firma digital
        return "signature_example";  // Aquí va la firma real generada
    }

    // Método para verificar la firma digital
    public static boolean verifySignature(String data, String signature, PublicKey publicKey) {
        try {
            // Convertir la firma (en Base64) de String a un arreglo de bytes
            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            
            // Crear una instancia de la clase Signature para verificar la firma
            Signature sig = Signature.getInstance("SHA256withRSA"); // Asegúrate de usar el algoritmo correcto
            
            // Inicializar el objeto Signature con la clave pública
            sig.initVerify(publicKey);
            
            // Actualizar el objeto Signature con los datos
            sig.update(data.getBytes());
            
            // Verificar si la firma es válida
            return sig.verify(signatureBytes);
        } catch (Exception e) {
            // Manejar excepciones como problemas con la clave o el algoritmo de firma
            e.printStackTrace();
            return false;
        }
    }
}
