package com.example.securerepo.crypto;

import com.example.securerepo.model.Note;
import com.example.securerepo.utils.BytesConverter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

            return new Note(cipher.doFinal(title),cipher.doFinal(body));
         } catch (Exception e){
            e.printStackTrace();
         }
      return null;
   }

    public static Note encryptNote (Note newNote, char[] password){
        try {
            SecretKeySpec secretKeySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            byte [] title = newNote.getTitle();
            byte [] body = newNote.getBody();
            int id = newNote.getId();
            return new Note(id, cipher.doFinal(title),cipher.doFinal(body));
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Note(new byte [0], new byte [0] );
    }

   public static Note decryptNote (byte[] title, byte[] body, char[] password) throws  Exception{

         SecretKeySpec secretKeySpec = generateKey(password);
         Cipher cipher = Cipher.getInstance("AES");
         if (secretKeySpec != null){
             cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         }

         return new Note(cipher.doFinal(title), cipher.doFinal(body));

   }

   public static Note decryptNote (Note encryptNote, char [] password) throws Exception{

       SecretKeySpec secretKeySpec = generateKey(password);
       Cipher cipher = Cipher.getInstance("AES");
       if (secretKeySpec != null){
           cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
       }
       byte [] title = encryptNote.getTitle();
       byte [] body = encryptNote.getBody();
       int id = encryptNote.getId();

       return new Note(id, cipher.doFinal(title), cipher.doFinal(body));
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
