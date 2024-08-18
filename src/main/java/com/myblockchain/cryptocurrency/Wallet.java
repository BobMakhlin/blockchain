package com.myblockchain.cryptocurrency;

import com.myblockchain.utils.CryptographyHelper;
import com.myblockchain.blockchain.Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Wallet {
    private PrivateKey privateKey; // signing.
    private PublicKey publicKey;   // verification.
    private Blockchain blockchain;

    public Wallet(Blockchain blockchain) {
        var pair = CryptographyHelper.generateKeyPair();
        privateKey = pair.getPrivate();
        publicKey = pair.getPublic();
        this.blockchain = blockchain;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Transaction send(PublicKey receiver, double amount) {
        if (calcBalance() < amount) {
            return null;
        }
        var inputs = new ArrayList<TransactionInput>();
        for (var e : blockchain.getUTXOs().entrySet()) {
            if (e.getValue().isMine(publicKey)) {
                inputs.add(new TransactionInput(e.getValue().getId()));
            }
        }
        var tran = new Transaction(publicKey, receiver, amount, inputs, blockchain);
        tran.sign(privateKey);
        return tran;
    }

    public double calcBalance() {
        var balance = 0.0;
        for (var e : blockchain.getUTXOs().entrySet()) {
            if (e.getValue().isMine(publicKey)) {
                balance += e.getValue().getAmount();
            }
        }
        return balance;
    }
}
