package com.example.securerepo.crypto;

import com.example.securerepo.model.PasswordChecker;
import com.example.securerepo.utils.BytesConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordCheckerCipher {

    public static PasswordChecker encryptChecker (byte[] bytesToCheck, char[] password){
        try {
            SecretKeySpec secretKeySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            return new PasswordChecker(cipher.doFinal(bytesToCheck));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void decryptChecker (PasswordChecker checker, char [] password) throws Exception{

        SecretKeySpec secretKeySpec = generateKey(password);
        Cipher cipher = Cipher.getInstance("AES");
        if (secretKeySpec != null){
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        }
        checker.setBytesToCheck(cipher.doFinal(checker.getBytesToCheck()));

    }



    private static SecretKeySpec generateKey(char[] password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte [] passwrd = BytesConverter.charToBytes(password);
            digest.update(passwrd,0,passwrd.length);
            byte[] key = digest.digest();
            return  new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }
}
