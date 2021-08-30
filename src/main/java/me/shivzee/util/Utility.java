package me.shivzee.util;

import java.util.Random;

/**
 * The Utility Class for utility ig lmao
 */
public class Utility {

    private static final String regex = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    /**
     * Generates a Random String
     *
     * @param length the String Length
     * @return String
     */
    public static String createRandomString(int length){
        String randomString = "";
        for(int i=0;i<length;i++){
            randomString += regex.charAt(random.nextInt(regex.length()));
        }
        return randomString;
    }
}
