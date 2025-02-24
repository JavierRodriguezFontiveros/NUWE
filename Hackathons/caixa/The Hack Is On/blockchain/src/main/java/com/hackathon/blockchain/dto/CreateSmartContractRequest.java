package com.hackathon.blockchain.dto;

public class CreateSmartContractRequest {
    private String contractId;
    private String contractName;
    private String conditionExpression;
    private String action;
    private String actionValue;
    private String issuerWalletId;

    // Getters y setters
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

    public String getIssuerWalletId() {
        return issuerWalletId;
    }

    public void setIssuerWalletId(String issuerWalletId) {
        this.issuerWalletId = issuerWalletId;
    }
}
