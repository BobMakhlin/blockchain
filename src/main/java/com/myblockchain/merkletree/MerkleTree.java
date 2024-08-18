package com.myblockchain.merkletree;

import com.myblockchain.cryptocurrency.Transaction;
import com.myblockchain.utils.CryptographyHelper;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    List<Transaction> transactions;

    public MerkleTree(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String buildMerkleRoot() {
        var hashedTransactions = transactions.stream().map(Transaction::getTransactionId).toList();
        return buildRecursive(hashedTransactions).get(0);
    }

    private List<String> buildRecursive(List<String> hashes) {
        if (hashes.size() == 1) {
            return hashes;
        }
        var layer = new ArrayList<String>();
        for (var i = 0; i < hashes.size(); i += 2) {
            var left = hashes.get(i);
            var right = i + 1 == hashes.size() ? hashes.get(i) : hashes.get(i + 1);
            var node = mergeHashes(left, right);
            layer.add(node);
        }
        return buildRecursive(layer);
    }

    private String mergeHashes(String hashA, String hashB) {
        return CryptographyHelper.getSHA256Hash(hashA.concat(hashB));
    }
}
