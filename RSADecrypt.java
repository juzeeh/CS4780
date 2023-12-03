package Project2;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSADecrypt {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java RSADecrypt <encrypted_file> <private_key_file>");
            return;
        }

        String encryptedFile = args[0];
        String privateKeyFile = args[1];
        String decryptedFile = "test.dec";

        try {
            // Read private key from file
            BufferedReader keyReader = new BufferedReader(new FileReader(privateKeyFile));

            String dLine = keyReader.readLine();
            String nLine = keyReader.readLine();

            if (dLine == null || nLine == null) {
                System.out.println("Invalid key file format.");
                keyReader.close();
                return;
            }

            BigInteger d = new BigInteger(dLine.split(" = ")[1]);
            BigInteger n = new BigInteger(nLine.split(" = ")[1]);

            keyReader.close();

            // Read encrypted file and decrypt blocks
            BufferedReader fileReader = new BufferedReader(new FileReader(encryptedFile, StandardCharsets.UTF_8));
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(decryptedFile, StandardCharsets.UTF_8));

            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] encryptedBlocks = line.split(" ");

                for (String encryptedBlock : encryptedBlocks) {
                    BigInteger encryptedNum = new BigInteger(encryptedBlock);
                    BigInteger decryptedBlock = encryptedNum.modPow(d, n);

                    // Convert numbers back to characters
                    int decryptedInt = decryptedBlock.intValue();
                    char decryptedChar = (char) decryptedInt;

                    if (decryptedInt == 26) {
                        fileWriter.write(' '); // Space
                    } else {
                        fileWriter.write(decryptedChar);
                    }
                }
            }

            // Close file readers and writers
            fileReader.close();
            fileWriter.close();

            System.out.println("Decryption complete. Decrypted file: " + decryptedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
