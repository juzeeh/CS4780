package Project2;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAGenKey {

    public static void main(String[] args) {
    	if (args.length == 1) {
            // If only one argument is provided, generate random p, q, and e
            int k = Integer.parseInt(args[0]);
            generateRandomKeys(k);
        } else if (args.length == 3) {
            // If three arguments are provided, use provided p, q, and e
            BigInteger p = new BigInteger(args[0]);
            BigInteger q = new BigInteger(args[1]);
            BigInteger e = new BigInteger(args[2]);
            generateKeys(p, q, e);
        } else {
            System.out.println("Invalid number of arguments.");
        }
    }

    private static void generateRandomKeys(int k) {
        SecureRandom random = new SecureRandom();

        // Generate random prime numbers p and q
        BigInteger p = BigInteger.probablePrime(k, random);
        BigInteger q = BigInteger.probablePrime(k, random);

        // Calculate n = p * q
        BigInteger n = p.multiply(q);

        // Calculate Euler's totient function: phi(n) = (p-1)(q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e such that 1 < e < phi(n) and e is coprime to phi(n)
        BigInteger e;
        do {
            e = new BigInteger(k, random);
        } while (e.compareTo(BigInteger.ONE) <= 0 || e.compareTo(phi) >= 0 || !e.gcd(phi).equals(BigInteger.ONE));

        generateKeys(p, q, e);
    }

    private static void generateKeys(BigInteger p, BigInteger q, BigInteger e) {
        // Calculate n = p * q
        BigInteger n = p.multiply(q);

        // Calculate Euler's totient function: phi(n) = (p-1)(q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Calculate the modular multiplicative inverse of e mod phi(n) to get d
        BigInteger d = e.modInverse(phi);

        // Save public key to pub_key.txt
        saveToFile("pub_key.txt", "e = " + e + "\nn = " + n);

        // Save private key to pri_key.txt
        saveToFile("pri_key.txt", "d = " + d + "\nn = " + n);
    }

    private static void saveToFile(String filename, String content) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
