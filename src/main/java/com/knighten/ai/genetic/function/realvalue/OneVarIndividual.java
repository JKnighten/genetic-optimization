package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.Individual;

/**
 * Represents an individual when optimizing a one variable real valued function. Contains a single x value that is a
 * guess at the x value that generates the optimal value.
 */
public class OneVarIndividual extends Individual<Double> {

    /**
     * Creates an OneVarIndividual and assign it an x value.
     *
     * @param xValue the individual's x value
     */
    public OneVarIndividual(double xValue) {
        this.setGenes(xValue);
    }

    public void setGenes(double genes) {

        // Catch NaN Or Infinity
        if (!Double.isFinite(genes))
            throw new IllegalArgumentException("genes Cannot Be NaN or Infinite: " + genes + " was found");

        super.setGenes(genes);
    }

    /**
     * Returns the string representation of the individual's x value.
     *
     * @return the x value as a string
     */
    @Override
    public String toString() {
        return Double.toString(this.getGenes());
    }

}
