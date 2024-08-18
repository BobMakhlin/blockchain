package com.myblockchain;

import com.myblockchain.blockchain.Block;
import com.myblockchain.blockchain.Blockchain;
import com.myblockchain.blockchain.Miner;
import com.myblockchain.cryptocurrency.TransactionOutput;
import com.myblockchain.cryptocurrency.Wallet;
import com.myblockchain.utils.Constants;
import com.myblockchain.cryptocurrency.Transaction;
import com.myblockchain.ecc.EllipticCurveCryptography;
import com.myblockchain.ecc.Point;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigDecimal;
import java.security.Security;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        eccCrackingExample(10_000_000);
    }

    private static void ellipticCurveDiffieHellman() {
        // Elliptic Curve Diffie-Hellman Algorithm
        var ecc = new EllipticCurveCryptography(new BigDecimal("3"), new BigDecimal("7"));
        var generator = new Point(new BigDecimal("-2"), BigDecimal.ONE);
        var random = new Random();

        var a = random.nextInt(10000);
        var b = random.nextInt(10000);

        // Public Keys - points on elliptic curve.
        var alicePublicKey = ecc.doubleAndAdd(a, generator);
        var bobPublicKey = ecc.doubleAndAdd(b, generator);

        // Private Keys
        var alicePrivateKey = ecc.doubleAndAdd(a, bobPublicKey); // a * b * generator(x1,y1)
        var bobPrivateKey = ecc.doubleAndAdd(b, alicePublicKey); // b * a * generator(x1,y1)

        // ... unfortunately the scale might be different (non production ready)
        // alicePrivateKey == bobPrivateKey
        System.out.println(alicePrivateKey);
        System.out.println(bobPrivateKey);

        // Public:
        // generator point,
        // public keys
        // Private:
        // a, b
        // private keys
        // Alice and Bob exchange their public keys with each other, and generate the same private key.

        // A hacker wants to find a or b in order to generate the private key,
        // he has to brute-force... xP = R, x - ?
    }

    private static void eccCrackingExample(int n) {
        var ecc = new EllipticCurveCryptography(BigDecimal.ZERO, new BigDecimal("7"));
        var p = new Point(new BigDecimal("2"), new BigDecimal("5"));

        var startTime = System.nanoTime();
        var r = ecc.doubleAndAdd(n, p);
        var endTime = System.nanoTime();
        System.out.println("Elapsed time (double and add): " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println(r);

        startTime = System.nanoTime();
        var crackedN = ecc.solveDLP(r, p);
        endTime = System.nanoTime();
        System.out.println("Elapsed time (cracking): " + (endTime - startTime) / 1_000_000.0 + " ms");
        System.out.println(crackedN);
    }

    private static void blockchainExample() {
        Security.addProvider(new BouncyCastleProvider());
        var blockchain = new Blockchain();
        var userA = new Wallet(blockchain);
        var userB = new Wallet(blockchain);
        var userC = new Wallet(blockchain);
        var lender = new Wallet(blockchain);
        var miner = new Miner(blockchain);

        // GENESIS BLOCK
        var genesisTran = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 50, null, blockchain);
        genesisTran.sign(lender.getPrivateKey());
        var tranOutput = new TransactionOutput(genesisTran.getTransactionId(), userA.getPublicKey(), genesisTran.getAmount());
        genesisTran.getOutputs().add(tranOutput);
        blockchain.getUTXOs().put(tranOutput.getId(), tranOutput);

        var genesisBlock = new Block(Constants.GENESIS_PREV_HASH, 1);
        genesisBlock.addTran(genesisTran);
        miner.mine(genesisBlock);

        System.out.println("Lender:" + lender.calcBalance());
        System.out.println("User A:" + userA.calcBalance());
        System.out.println("User B:" + userB.calcBalance());
        System.out.println("User C:" + userC.calcBalance());

        // BLOCK 1
        var block1 = new Block(genesisBlock.getHash(), 2);
        var tran1 = userA.send(userB.getPublicKey(), 10);
        block1.addTran(tran1);
        var tran2 = userA.send(userC.getPublicKey(), 2);
        block1.addTran(tran2);
        var tran3 = userA.send(userB.getPublicKey(), 3);
        block1.addTran(tran3);
        miner.mine(block1);

        System.out.println("Lender:" + lender.calcBalance());
        System.out.println("User A:" + userA.calcBalance());
        System.out.println("User B:" + userB.calcBalance());
        System.out.println("User C:" + userC.calcBalance());

        // BLOCK 2
        var block2 = new Block(block1.getHash(), 3);
        var tran4 = userB.send(userA.getPublicKey(), 3);
        block2.addTran(tran4);
        var tran5 = userC.send(userA.getPublicKey(), 1);
        block2.addTran(tran5);
        miner.mine(block2);

        System.out.println("Lender:" + lender.calcBalance());
        System.out.println("User A:" + userA.calcBalance());
        System.out.println("User B:" + userB.calcBalance());
        System.out.println("User C:" + userC.calcBalance());

        System.out.println("Miner Reward:" + miner.getReward());
    }
}