package com.myblockchain.cryptocurrency;

import com.myblockchain.blockchain.Blockchain;
import com.myblockchain.utils.CryptographyHelper;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey receiver;
    private double amount;
    private byte[] signature; // of the sender.
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs = new ArrayList<>();
    private Blockchain blockchain;

    public Transaction(PublicKey sender, PublicKey receiver, double amount,
                       List<TransactionInput> inputs, Blockchain blockchain) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.blockchain = blockchain;
        this.calculateHash();
    }

    public void sign(PrivateKey privateKey) {
        this.signature = CryptographyHelper.sign(metadata(), privateKey);
    }

    public boolean verify() {
        if (!verifySignature()) {
            return false;
        }

        for (var transactionInput : inputs) {
            transactionInput.setUtxo(blockchain.getUTXOs().get(transactionInput.getTransactionOutputId()));
        }

        outputs.add(new TransactionOutput(this.transactionId, this.receiver, this.amount));
        outputs.add(new TransactionOutput(this.transactionId, this.sender, calculateInputsSum() - this.amount));

        for (var transactionOutput : outputs) {
            blockchain.getUTXOs().put(transactionOutput.getId(), transactionOutput);
        }

        for (var transactionInput : inputs) {
            if (transactionInput.getUtxo() != null) {
                blockchain.getUTXOs().remove(transactionInput.getUtxo().getId());
            }
        }

        return true;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public byte[] getSignature() {
        return signature;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    private boolean verifySignature() {
        return CryptographyHelper.verify(metadata(), signature, sender);
    }

    private double calculateInputsSum() {
        var sum = 0.0;
        for (var transactionInput : inputs) {
            if (transactionInput.getUtxo() != null) {
                sum += transactionInput.getUtxo().getAmount();
            }
        }
        return sum;
    }

    private void calculateHash() {
        this.transactionId = CryptographyHelper.getSHA256Hash(metadata());
    }

    private String metadata() {
        return sender.toString()
                .concat(receiver.toString())
                .concat(Double.toString(amount));
    }
}
