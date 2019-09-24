package com.alekseymakarov.locknote.crypto;

import com.alekseymakarov.locknote.model.Note;
import com.alekseymakarov.locknote.utils.BytesConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class NoteCipher {

   public static Note encryptNote (SecretKeySpec secretKeySpec, Cipher cipher, byte[] title, byte[] body, long date){
         try {

            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            return new Note(cipher.doFinal(title),cipher.doFinal(body), date);
         } catch (Exception e){
            e.printStackTrace();
         }
      return null;
   }

    public static void encryptNote (SecretKeySpec secretKeySpec,Cipher cipher, Note note){
        try {

            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            note.setTitle(cipher.doFinal(note.getTitle()));
            note.setBody(cipher.doFinal(note.getBody()));
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static List<Note> encryptNotes (SecretKeySpec secretKeySpec, Cipher cipher, List<Note> notes){
       List<Note> encryptedNotes = new ArrayList<>(notes.size());
        try {

            if (secretKeySpec != null){
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }
            for (Note note:notes){
                note.setTitle(cipher.doFinal(note.getTitle()));
                note.setBody(cipher.doFinal(note.getBody()));
                encryptedNotes.add(note);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
            return encryptedNotes;
    }

   public static void decryptNote (SecretKeySpec secretKeySpec,Cipher cipher, Note note) throws Exception{

       if (secretKeySpec != null){
           cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
       }
       note.setTitle(cipher.doFinal(note.getTitle()));
       note.setBody(cipher.doFinal(note.getBody()));

   }

   public static SecretKeySpec generateKey(char[] password){
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
