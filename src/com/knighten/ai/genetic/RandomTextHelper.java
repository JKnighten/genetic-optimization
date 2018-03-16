package com.knighten.ai.genetic;

import java.util.Random;

public class RandomTextHelper {

    static final String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";

    public static String generateString(int length){
        StringBuilder sBuilder = new StringBuilder();
        Random random = new Random();

        for(int i=0; i<length; i++)
            sBuilder.append(validChars.charAt(random.nextInt(validChars.length())));

        return sBuilder.toString();
    }

    public static char generateChar(){
        Random random = new Random();
        return validChars.charAt(random.nextInt(validChars.length()));
    }
}
