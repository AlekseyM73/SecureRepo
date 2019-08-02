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

    public static void encryptNote (Note note, char[] password){
        try {
            SecretKeySpec secretKeySpec = generateKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            note.setTitle(cipher.doFinal(note.getTitle()));
            note.setBody(cipher.doFinal(note.getBody()));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

   public static Note decryptNote (byte[] title, byte[] body, char[] password) throws  Exception{

         SecretKeySpec secretKeySpec = generateKey(password);
         Cipher cipher = Cipher.getInstance("AES");
         if (secretKeySpec != null){
             cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
         }

         return new Note(cipher.doFinal(title), cipher.doFinal(body));

   }

   public static void decryptNote (Note note, char [] password) throws Exception{

       SecretKeySpec secretKeySpec = generateKey(password);
       Cipher cipher = Cipher.getInstance("AES");
       if (secretKeySpec != null){
           cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
       }
       note.setTitle(cipher.doFinal(note.getTitle()));
       note.setBody(cipher.doFinal(note.getBody()));

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
