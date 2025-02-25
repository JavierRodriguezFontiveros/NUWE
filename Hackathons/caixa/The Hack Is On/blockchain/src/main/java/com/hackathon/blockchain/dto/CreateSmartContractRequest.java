package com.hackathon.blockchain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateSmartContractRequest {

    @JsonProperty("contractId")
    private Long contractId;

    @JsonProperty("name")
    private String contractName;

    @JsonProperty("conditionExpression")
    private String conditionExpression;

    @JsonProperty("action")
    private String action;

    @JsonProperty("actionValue")
    private String actionValue;

    @JsonProperty("issuerWalletId")
    private String issuerWalletId;  // Cambiado de Long a String

    // Getters y setters
    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
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
