package com.myblockchain.utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class CryptographyHelper {
    public static String getSHA256Hash(String input) {
        try {
            var md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            var hexString = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] sign(String message, PrivateKey privateKey) {
        try {
            // BC - Bouncy Castle
            var signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            return signature.sign();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static boolean verify(String message, byte[] signature, PublicKey publicKey) {
        try {
            // BC - Bouncy Castle
            var ecdsaSignature = Signature.getInstance("ECDSA", "BC");
            ecdsaSignature.initVerify(publicKey);
            ecdsaSignature.update(message.getBytes(StandardCharsets.UTF_8));
            return ecdsaSignature.verify(signature);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static KeyPair generateKeyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");
            var spec = new ECGenParameterSpec("prime256v1");
            keyPairGenerator.initialize(spec);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
