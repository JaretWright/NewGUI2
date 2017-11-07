/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
 
public class SHAPasswordGenerator {
    
    public static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     
     
    //Add salt
    public static byte[] getSalt() throws NoSuchAlgorithmException
    {
        //create a SecureRandon instance using the SHA1PRNG alogorithm
        //SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        SecureRandom sr = SecureRandom.getInstanceStrong();  //beter than SHA1PRNG which can be deterministic on windows machines
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }
}