package com.knighten.ai.genetic.stringmatch;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * Helps generate random strings and characters using a set of characters.
 */
public class RandomTextHelper {

    /**
     * Used to generate random numbers to generate random text data.
     */
    private Random random;

    /**
     * The set of characters that are used for generating characters and strings.
     */
    public String validChars;

    /**
     * Creates a RandomTextHelper object that uses the supplied Random object.
     *
     * @param random     object used to generate random numbers
     * @param validChars string of valid characters to generate
     */
    public RandomTextHelper(Random random, String validChars) {
        if (random == null)
            throw new IllegalArgumentException("A RandomTextHelper Random Object Cannot Be Null");

        if (validChars == null)
            throw new IllegalArgumentException("Valid Characters Cannot Be Null");

        if (validChars.isEmpty())
            throw new IllegalArgumentException("Valid Characters Cannot Be Empty");

        this.random = random;
        this.validChars = validChars;
    }

    /**
     * Generates random strings using validChars.
     *
     * @param length the length of string to be generated
     * @return a random string
     */
    public String generateString(int length) {
        if (length <= 0)
            throw new IllegalArgumentException("The Length Of A Generated String Must Be Greater Than 0");

        return this.random.ints(length, 0, this.validChars.length())
                .mapToObj((i) -> Character.toString(this.validChars.charAt(i)))
                .collect(Collectors.joining());
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
