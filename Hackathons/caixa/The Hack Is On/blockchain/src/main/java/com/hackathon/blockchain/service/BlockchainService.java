package com.hackathon.blockchain.service;

import com.hackathon.blockchain.model.Block;
import com.hackathon.blockchain.model.Transaction;
import com.hackathon.blockchain.repository.BlockRepository;
import com.hackathon.blockchain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {

    private final BlockRepository blockRepository;
    private final TransactionRepository transactionRepository; // Para manejar las transacciones

    // Lista para almacenar transacciones pendientes
    private List<Transaction> pendingTransactions = new ArrayList<>();

    @Autowired
    public BlockchainService(BlockRepository blockRepository, TransactionRepository transactionRepository) {
        this.blockRepository = blockRepository;
        this.transactionRepository = transactionRepository;

        // Verificar si la blockchain ya tiene bloques, si no, crear el bloque génesis
        if (blockRepository.count() == 0) {
            Block genesisBlock = new Block("Genesis Block", "0", "0", 0);
            genesisBlock.setHash(genesisBlock.calculateHash());  // Calcular hash del bloque génesis
            blockRepository.save(genesisBlock);
        }
    }

    // Método para agregar transacciones pendientes
    public void addTransaction(Transaction transaction) {
        // Asignar el estado inicial "PENDING"
        transaction.setStatus("PENDING");
        
        // Guardar la transacción en la base de datos
        transactionRepository.save(transaction);  // Persistir en DB
        
        // Añadir la transacción a la lista de transacciones pendientes
        pendingTransactions.add(transaction);
    }

    // Método para minar un bloque con las transacciones pendientes
    public String mineBlock() {
        // Verificar si hay transacciones pendientes
        if (pendingTransactions.isEmpty()) {
            return "{\"message\": \"❌ No pending transactions to mine.\"}";
        }

        // Obtener el último bloque de la cadena
        Block lastBlock = blockRepository.findTopByOrderByBlockIndexDesc()
                                        .orElseThrow(() -> new RuntimeException("No blocks found in the blockchain"));

        // Agrupar las transacciones pendientes en formato String
        StringBuilder transactionData = new StringBuilder("Transactions: ");
        for (Transaction tx : pendingTransactions) {
            transactionData.append(tx.getTxId()).append(" ");
            // Actualizar el estado de las transacciones a "COMPLETED"
            tx.setStatus("COMPLETED");
            transactionRepository.save(tx);  // Guardar el estado actualizado
        }

        // Crear el nuevo bloque con las transacciones
        Block newBlock = new Block(transactionData.toString(), lastBlock.getHash(), "", lastBlock.getBlockIndex() + 1);
        newBlock.setHash(newBlock.calculateHash());  // Calcular el hash del nuevo bloque
        
        // Guardar el bloque en la base de datos
        blockRepository.save(newBlock);

        // Limpiar las transacciones pendientes después de minar el bloque
        pendingTransactions.clear();

        return "{\"message\": \"Block mined: " + newBlock.getHash() + "\"}";
    }

    // Método para validar la cadena de bloques
    public boolean isChainValid() {
        List<Block> chain = blockRepository.findAll();

        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);
    
            String recalculatedHash = current.calculateHash();
            if (!current.getHash().equals(recalculatedHash)) {
                System.out.println("❌ Hash mismatch in block " + current.getBlockIndex());
                return false;
            }
    
            if (!current.getPreviousHash().equals(previous.getHash())) {
                System.out.println("❌ Previous hash mismatch in block " + current.getBlockIndex());
                return false;
            }
        }
    
        return true;
    }
}
