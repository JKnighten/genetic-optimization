package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.Individual;

public class OneVarIndividual extends Individual<Double>{

    public OneVarIndividual(double xValue) {
        this.setGenes(xValue);
    }

    @Override
    public String toString() {
        return Double.toString(this.getGenes());
    }
}
