package com.nuevatel.bcf;

import java.rmi.server.UID;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by asalazar on 6/23/15.
 */
public class GeneratingUniqueID {
//    /**
//     * Build and display some UID objects.
//     */
//    public static void main (String... arguments) {
//        for (int idx = 0; idx < 10; ++idx) {
//            UID userId = new UID();
//            System.out.println("User Id: " + userId);
//        }
//    }

//    public static void main (String... arguments) {
//        try {
//            //Initialize SecureRandom
//            //This is a lengthy operation, to be done only upon
//            //initialization of the application
//            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
//
//            //generate a random number
//            String randomNum = new Integer(prng.nextInt()).toString();
//
//            //get its digest
//            MessageDigest sha = MessageDigest.getInstance("SHA-1");
//            byte[] result =  sha.digest(randomNum.getBytes());
//
//            System.out.println("Random number: " + randomNum);
//            System.out.println("Message digest: " + hexEncode(result));
//        }
//        catch (NoSuchAlgorithmException ex) {
//            System.err.println(ex);
//        }
//    }
//    /**
//     * The byte[] returned by MessageDigest does not have a nice
//     * textual representation, so some form of encoding is usually performed.
//     *
//     * This implementation follows the example of David Flanagan's book
//     * "Java In A Nutshell", and converts a byte array into a String
//     * of hex characters.
//     *
//     * Another popular alternative is to use a "Base64" encoding.
//     */
//    static private String hexEncode(byte[] aInput){
//        StringBuilder result = new StringBuilder();
//        char[] digits = {'0', '1', '2', '3', '4','5','6','7','8','9','a','b','c','d','e','f'};
//        for (int idx = 0; idx < aInput.length; ++idx) {
//            byte b = aInput[idx];
//            result.append(digits[ (b&0xf0) >> 4 ]);
//            result.append(digits[ b&0x0f]);
//        }
//        return result.toString();
//    }

    public static final void main(String... aArgs){
        //generate random UUIDs
        UUID idOne = UUID.randomUUID();
        UUID idTwo = UUID.randomUUID();
        log("UUID One: " + idOne);
        log("UUID Two: " + idTwo);
    }

    private static void log(Object aObject){
        System.out.println( String.valueOf(aObject) );
    }
}
