package com.hackathon.blockchain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class SmartContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Esta es la clave primaria generada por la base de datos

    private String contractId;  // Se usa String porque en el DTO es String
    private String contractName;

    @ManyToOne
    @JoinColumn(name = "wallet_id")  // Relación con Wallet (quien ejecuta el contrato)
    private Wallet associatedWallet;

    @OneToMany(mappedBy = "smartContract")  // Relación con la lista de activos involucrados
    private List<Asset> assetsInvolved;

    private String contractStatus;

    // Nuevas propiedades necesarias para la lógica de los errores
    private String conditionExpression;  // Expresión de la condición del contrato
    private String action;               // Acción que se realiza en el contrato
    private String actionValue;          // Valor de la acción
    private String digitalSignature;     // Firma digital

    // Constructor actualizado para tomar los parámetros como String y Wallet en lugar de issuerWalletId
    public SmartContract(String contractId, String contractName, Wallet associatedWallet, 
                         String conditionExpression, String action, String actionValue) {
        this.contractId = contractId;
        this.contractName = contractName;
        this.associatedWallet = associatedWallet;
        this.contractStatus = "Active";  // Un estado por defecto
        this.conditionExpression = conditionExpression;
        this.action = action;
        this.actionValue = actionValue;
    }

    // Métodos para ejecutar las lógicas del contrato
    public boolean executeContract() {
        // Verifica si el contrato está activo
        if ("Active".equals(contractStatus)) {
            // Verifica si la wallet tiene suficientes fondos
            if (associatedWallet.getBalance() >= calculateRequiredFunds()) {
                // Si los fondos son suficientes, ejecuta el contrato
                contractStatus = "Completed";
                System.out.println("Ejecutando contrato: " + contractName);
                return true;
            } else {
                System.out.println("Fondos insuficientes para ejecutar el contrato.");
            }
        }
        return false;
    }

    // Método para calcular los fondos necesarios según los activos involucrados en el contrato
    private double calculateRequiredFunds() {
        double requiredAmount = 0.0;
        if (assetsInvolved != null) {
            for (Asset asset : assetsInvolved) {
                requiredAmount += asset.getValue() * asset.getQuantity();
            }
        }
        return requiredAmount;
    }

    // Getters y Setters tradicionales
    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Wallet getAssociatedWallet() {
        return associatedWallet;
    }

    public void setAssociatedWallet(Wallet associatedWallet) {
        this.associatedWallet = associatedWallet;
    }

    public List<Asset> getAssetsInvolved() {
        return assetsInvolved;
    }

    public void setAssetsInvolved(List<Asset> assetsInvolved) {
        this.assetsInvolved = assetsInvolved;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    // Nuevas propiedades
    public String getConditionExpression() {
        return conditionExpression;
    }

    public void setConditionExpression(String conditionExpression) {
        this.conditionExpression = conditionExpression;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    // Métodos adicionales para devolver el tipo y el nombre
    public String getType() {
        return this.contractId;  // Ejemplo de uso de contractId
    }

    public String getName() {
        return this.contractName;
    }
}
