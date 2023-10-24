package ms.study.kurly.common.encryption;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String SECRET_KEY = "KurlySecretKey12";

    public static String encrypt(Long id) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(String.valueOf(id).getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static Long decrypt(String encryptedId) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedId));
        String decryptedIdString = new String(decryptedBytes);
        return Long.parseLong(decryptedIdString);
    }
}
