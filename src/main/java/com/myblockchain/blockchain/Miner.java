package com.myblockchain.blockchain;

import com.myblockchain.utils.Constants;

import java.util.Collections;

public class Miner {
    private final Blockchain blockchain;
    private double reward;

    public Miner(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void mine(Block block) {
        // Proof Of Work.
        int i = 0;
        do {
            block.incrementNonce();
            block.generateHash();
//            System.out.println("hash:" + block.getHash());
            i++;
        } while (!isGoldenHash(block.getHash()));
        System.out.println("iterations count:" + i);
        System.out.println("Golden block:" + block);
        System.out.println("block nonce:" + block.getNonce());
        blockchain.addBlock(block);
        reward += Constants.REWARD;
    }

    public double getReward() {
        return reward;
    }

    private boolean isGoldenHash(String hash) {
        return hash.startsWith(String.join("", Collections.nCopies(Constants.DIFFICULTY, "0")));
    }
}
