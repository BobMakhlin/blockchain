package com.myblockchain.blockchain;

import com.myblockchain.cryptocurrency.TransactionOutput;

import java.util.*;

public class Blockchain {
    List<Block> blocks = new LinkedList<>();
    Map<String, TransactionOutput> utxos = new HashMap<>();

    public void addBlock(Block block) {
        blocks.add(block);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getSize() {
        return blocks.size();
    }

    public Map<String, TransactionOutput> getUTXOs() {
        return utxos;
    }
}
