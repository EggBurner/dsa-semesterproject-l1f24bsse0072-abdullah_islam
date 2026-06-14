import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

public class Crypto {

    public static String encrypt(String text, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(text.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String encryptedText, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

            return new String(decrypted);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}