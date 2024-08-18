package com.myblockchain.cryptocurrency;

import com.myblockchain.utils.CryptographyHelper;

import java.security.PublicKey;

public class TransactionOutput {
    private final String id;
    private String parentTransactionId;
    private PublicKey receiver;
    private double amount;

    public TransactionOutput(String parentTransactionId, PublicKey receiver, double amount) {
        this.parentTransactionId = parentTransactionId;
        this.receiver = receiver;
        this.amount = amount;
        id = CryptographyHelper.getSHA256Hash(receiver.toString()
                .concat(Double.toString(amount))
                .concat(parentTransactionId));
    }

    public boolean isMine(PublicKey key) {
        return receiver.equals(key);
    }

    public String getId() {
        return id;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
