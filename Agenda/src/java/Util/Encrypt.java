/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.security.MessageDigest;

/**
 *
 * @author j.reyes
 */
public class Encrypt {
    public static String sha512(String text) throws RuntimeException{
        StringBuilder builder =  new StringBuilder();
        
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.update(text.getBytes());
            
            final byte[] byteEncrypt = digest.digest();
            
            for (int index = 0; index < byteEncrypt.length; index++) 
               builder.append(Integer.toString((byteEncrypt[index] & 0xff) + 0x100, 16).substring(1));
            
            return builder.toString();
            
        } catch (Exception error) {
            throw new RuntimeException("Error Encriptando", error);
        }
    }    
}
