package com.knighten.ai.genetic.stringmatch;

import java.util.Random;

/**
 * Set of static methods that help generate random strings and characters. Uses a predefined set of characters
 * that be used for selection.
 */
public class RandomTextHelper {

    /**
     * The set of characters that are used for generating characters and strings.
     */
    static final String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";

    /**
     * Generates random strings using validChars.
     *
     * @param length the length of string to be generated
     * @return a random string
     */
    public static String generateString(int length){
        StringBuilder sBuilder = new StringBuilder();
        Random random = new Random();

        for(int i=0; i<length; i++)
            sBuilder.append(validChars.charAt(random.nextInt(validChars.length())));

        return sBuilder.toString();
    }

    /**
     * Generates a random character from validChars.
     *
     * @return a single random character
     */
    public static char generateChar(){
        Random random = new Random();
        return validChars.charAt(random.nextInt(validChars.length()));
    }
}
