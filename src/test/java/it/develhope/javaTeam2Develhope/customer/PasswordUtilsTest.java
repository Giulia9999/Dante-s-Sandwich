package it.develhope.javaTeam2Develhope.customer;

import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PasswordUtilsTest {
    @Test
    public void testGeneratePasswordSalt() {
        // Act
        String salt = generatePasswordSalt();

        // Assert
        assertNotNull(salt);
    }

    @Test
    public void testGeneratePasswordHash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Arrange
        String password = "password";
        String salt = generatePasswordSalt();

        // Act
        String hash = generatePasswordHash(password, salt);

        // Assert
        assertNotNull(hash);
    }

    @Test
    public void testGeneratePasswordHash_WithInvalidAlgorithm() {
        // Arrange
        String password = "password";
        String salt = generatePasswordSalt();

        // Act
        String hash = generatePasswordHashWithInvalidAlgorithm(password, salt);

        // Assert
        assertNull(hash);
    }

    private String generatePasswordSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generatePasswordHash(String password, String salt){
        String algorithm = "PBKDF2WithHmacSHA256";
        int iterations = 65536;
        int keyLength = 128;
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(algorithm);
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
            SecretKey key = factory.generateSecret(spec);
            byte[] hashBytes = key.getEncoded();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generatePasswordHashWithInvalidAlgorithm(String password, String salt) {
        String algorithm = "InvalidAlgorithm"; // Using an invalid algorithm
        int iterations = 65536;
        int keyLength = 128;
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        SecretKeyFactory factory;
        try {
            factory = SecretKeyFactory.getInstance(algorithm);
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
            SecretKey key = factory.generateSecret(spec);
            byte[] hashBytes = key.getEncoded();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }
}
