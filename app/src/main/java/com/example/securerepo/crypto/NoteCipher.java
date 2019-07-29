package com.example.securerepo.crypto;

import com.example.securerepo.Utils.BytesConverter;
import com.example.securerepo.model.Note;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class NoteCipher {

   public static Note encryptNote (byte[] title, byte[] body, char[] password){
         try {
            SecretKeySpec secretKeySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            ArrayList<byte[]> bytes = new ArrayList<>();
            bytes.add(cipher.doFinal(title));
            bytes.add(cipher.doFinal(body));
            return new Note(cipher.doFinal(title),cipher.doFinal(body));
         } catch (Exception e){
            e.printStackTrace();
         }
      return null;
   }

   public static Note decryptNote (byte[] title, byte[] body, char[] password) throws  Exception{

         SecretKeySpec secretKeySpec = generateKey(password);
         Cipher cipher = Cipher.getInstance("AES");
         if (secretKeySpec != null){
             cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         }
         ArrayList<byte[]> bytes = new ArrayList<>();
         bytes.add(cipher.doFinal(title));
         bytes.add(cipher.doFinal(body));
         return new Note(cipher.doFinal(title), cipher.doFinal(body));

   }

   private static SecretKeySpec generateKey(char[] password){
      try {
         MessageDigest  digest = MessageDigest.getInstance("SHA-256");
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
