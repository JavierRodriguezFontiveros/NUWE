package com.hackathon.blockchain.service;

import com.hackathon.blockchain.model.SmartContract;
import com.hackathon.blockchain.model.Transaction;
import com.hackathon.blockchain.repository.SmartContractRepository;
import com.hackathon.blockchain.repository.TransactionRepository;
import com.hackathon.blockchain.utils.SignatureUtil;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Lazy;

import java.security.PublicKey;
import java.util.List;

@Service
public class SmartContractEvaluationService {

    private final SmartContractRepository smartContractRepository;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;
    private final WalletKeyService walletKeyService;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    public SmartContractEvaluationService(SmartContractRepository smartContractRepository,
                                          TransactionRepository transactionRepository,
                                          @Lazy WalletService walletService,
                                          WalletKeyService walletKeyService) {
        this.smartContractRepository = smartContractRepository;
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
        this.walletKeyService = walletKeyService;
    }

    public boolean verifyContractSignature(SmartContract contract) {
        try {
            // Convertir issuerWalletId de String a Long
            Long issuerWalletId = Long.parseLong(contract.getIssuerWalletId()); // Conversión de String a Long
            PublicKey issuerPublicKey = walletKeyService.getPublicKeyForWallet(issuerWalletId);
            
            if (issuerPublicKey == null) {
                return false;
            }
            
            String dataToSign = contract.getName() +
                                 contract.getConditionExpression() +
                                 contract.getAction() +
                                 contract.getActionValue() +
                                 contract.getIssuerWalletId();
                                 
            return SignatureUtil.verifySignature(dataToSign, contract.getDigitalSignature(), issuerPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public void evaluateSmartContracts() {
        List<SmartContract> contracts = smartContractRepository.findAll();
        List<Transaction> pendingTxs = transactionRepository.findByStatus("PENDING");

        for (Transaction tx : pendingTxs) {
            StandardEvaluationContext context = new StandardEvaluationContext();

            try {
                // Ahora estamos usando 'double' para el valor de 'amount'
                double amountValue = tx.getAmount();  // Directamente usar el valor de 'tx.getAmount()' que es un tipo 'double'
                context.setVariable("amount", amountValue);

                context.setVariable("txType", tx.getType());

                for (SmartContract contract : contracts) {
                    if (!verifyContractSignature(contract)) {
                        continue;
                    }

                    // Evaluar la condición del contrato usando SpEL
                    Expression exp = parser.parseExpression(contract.getConditionExpression());
                    Boolean conditionMet = exp.getValue(context, Boolean.class);

                    if (conditionMet != null && conditionMet) {
                        if ("CANCEL_TRANSACTION".equalsIgnoreCase(contract.getAction())) {
                            tx.setStatus("CANCELED");
                        } else if ("TRANSFER_FEE".equalsIgnoreCase(contract.getAction())) {
                            // Validación y conversión de 'actionValue' (String a Double)
                            Double actionValue = parseDoubleActionValue(contract.getActionValue());

                            // Si 'actionValue' no es nulo y diferente de 0.0, procedemos con la transferencia
                            if (actionValue != null && actionValue != 0.0) {
                                walletService.transferFee(tx, actionValue);
                            }
                            tx.setStatus("PROCESSED_CONTRACT");
                        }
                        transactionRepository.save(tx);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al evaluar transacción " + tx.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Método para convertir String a Double con validación y manejo de errores
    private Double parseDoubleActionValue(String actionValue) {
        if (actionValue != null && !actionValue.isEmpty()) {
            try {
                return Double.valueOf(actionValue);  // Convertir el String a Double
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir 'actionValue' a Double: " + actionValue);
            }
        }
        return 0.0;  // Valor por defecto
    }
}
