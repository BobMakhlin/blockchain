//package com.scheidtbachmann.frs.bos.merkletree;
//
//import org.junit.jupiter.api.Test;
//
//import java.security.NoSuchAlgorithmException;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class MerkleRootCalculatorTest {
//
//    @Test
//    public void fourTransactions() throws NoSuchAlgorithmException {
//        var transactions = List.of("Bob->Martin,20", "Lora->Viktor,78", "James->Bond,29.5", "Hugo->Grant,410");
//        String expectedMerkleRoot = "f97f20a8fa0aa363655b86e3a5b7861715c2508d6cad95babd9a6336fad98da0";
//        assertEquals(expectedMerkleRoot, new MerkleTree(transactions).buildMerkleRoot());
//    }
//
//    @Test
//    public void threeTransactions() throws NoSuchAlgorithmException {
//        var transactions = List.of("Bob->Martin,20", "Lora->Viktor,78", "James->Bond,29.5");
//        String expectedMerkleRoot = "f97e84309548b83409edfb79b86343f489ebe0a37eda7f695888480988a9ace8";
//        assertEquals(expectedMerkleRoot, new MerkleTree(transactions).buildMerkleRoot());
//    }
//}