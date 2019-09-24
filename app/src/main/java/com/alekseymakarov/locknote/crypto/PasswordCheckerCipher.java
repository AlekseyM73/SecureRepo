package com.alekseymakarov.locknote.crypto;

import com.alekseymakarov.locknote.model.PasswordChecker;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordCheckerCipher {

    public static PasswordChecker encryptChecker (SecretKeySpec secretKeySpec, Cipher cipher,byte[] bytesToCheck){
        try {

            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            return new PasswordChecker(cipher.doFinal(bytesToCheck));
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void decryptChecker (SecretKeySpec secretKeySpec, Cipher cipher,PasswordChecker checker) throws Exception{

        if (secretKeySpec != null){
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        }
        checker.setBytesToCheck(cipher.doFinal(checker.getBytesToCheck()));

    }

}
