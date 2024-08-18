package com.myblockchain.blockchain;

import com.myblockchain.cryptocurrency.Transaction;
import com.myblockchain.merkletree.MerkleTree;
import com.myblockchain.utils.Constants;
import com.myblockchain.utils.CryptographyHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {
    private int id;
    private int nonce;
    private long timestamp;
    private String hash;
    private String previousHash;
    private List<Transaction> transactions;

    public Block(String previousHash, int id) {
        this.previousHash = previousHash;
        this.id = id;
        this.timestamp = new Date().getTime();
        this.transactions = new ArrayList<>();
    }

    public void generateHash() {
        var data = String.format("%s%s%s%s%s", id, previousHash, timestamp, nonce,
                new MerkleTree(transactions).buildMerkleRoot());
        this.hash = CryptographyHelper.getSHA256Hash(data);
    }

    public boolean addTran(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if (!previousHash.equals(Constants.GENESIS_PREV_HASH)
                && !transaction.verify()) {
            return false;
        }
        transactions.add(transaction);
        return true;
    }

    public void incrementNonce() {
        nonce++;
    }

    public int getId() {
        return id;
    }

    public int getNonce() {
        return nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Block{" +
                "id=" + id +
                ", hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                '}';
    }
}
