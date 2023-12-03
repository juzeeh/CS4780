package Project2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

public class RSAEncrypt {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RSAEncrypt <input_file> <public_key_file>");
            return;
        }

        String inputFile = args[0];
        String publicKeyFile = args[1];
        String outputFile = "test.enc";

        try {
            // Read public key from file
            BufferedReader keyReader = new BufferedReader(new FileReader(publicKeyFile));

            String eLine = keyReader.readLine();
            String nLine = keyReader.readLine();

            if (eLine == null || nLine == null) {
                System.out.println("Invalid key file format.");
                keyReader.close();
                return;
            }

            BigInteger e = new BigInteger(eLine.split(" = ")[1]);
            BigInteger n = new BigInteger(nLine.split(" = ")[1]);

            keyReader.close();

            // Read input file and encrypt blocks
            BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile));

            StringBuilder block = new StringBuilder();
            int charInt;

            while ((charInt = fileReader.read()) != -1) {
                // Convert characters to numbers (00-26 encoding)
                if (charInt >= 'a' && charInt <= 'z') {
                    block.append(charInt - 'a');
                } else if (charInt == ' ') {
                    block.append(26); // 26 for space
                }

                // Encrypt and write block when it reaches the desired size
                if (block.length() >= 3) {
                    BigInteger blockNum = new BigInteger(block.toString());
                    BigInteger encryptedBlock = blockNum.modPow(e, n);
                    fileWriter.write(encryptedBlock.toString() + " ");
                    block.setLength(0); // Clear the block for the next iteration
                }
            }

            // Close file readers and writers
            fileReader.close();
            fileWriter.close();

            System.out.println("Encryption complete. Encrypted file: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
