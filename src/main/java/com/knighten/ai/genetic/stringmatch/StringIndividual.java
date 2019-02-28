package com.knighten.ai.genetic.stringmatch;

import com.knighten.ai.genetic.Individual;

/**
 * Represents an individual in the string matching problem. Stores a string and a fitness score.
 */
public class StringIndividual extends Individual<String> {

    /**
     * Creates a individual for the string matching problem. Stores the supplied string as its genes.
     *
     * @param stringValue the string contained in the individual
     */
    public StringIndividual(String stringValue){
        this.setGenes(stringValue);
    }

    /**
     * Returns the string representation of the individuals genes. Since the individual genes are a string, they are
     * directly returned.
     *
     * @return the individuals value
     */
    @Override
    public String toString(){ return this.getGenes(); }

}
