package com.knighten.ai.genetic.stringmatch;

import java.util.Random;

/**
 * Set of static methods that help generate random strings and characters. Uses a predefined set of characters that be
 * used for selection.
 */
public class RandomTextHelper {

    private Random random;

    public  RandomTextHelper(Random random) {
        this.random = random;
    }

    /**
     * The set of characters that are used for generating characters and strings.
     */
    private final String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";

    /**
     * Generates random strings using validChars.
     *
     * @param length the length of string to be generated
     * @return a random string
     */
    public String generateString(int length) {
        StringBuilder sBuilder = new StringBuilder();

        for(int i=0; i<length; i++)
            sBuilder.append(this.validChars.charAt(this.random.nextInt(this.validChars.length())));

        return sBuilder.toString();
    }

    /**
     * Generates a random character from validChars.
     *
     * @return a single random character
     */
    public char generateChar() {
        return this.validChars.charAt(this.random.nextInt(this.validChars.length()));
    }
}
