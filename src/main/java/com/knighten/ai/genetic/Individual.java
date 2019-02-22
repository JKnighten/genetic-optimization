package com.knighten.ai.genetic;

/**
 * Represents an individual in the population used in the genetic optimization algorithm.
 *
 * @param <V> represents the type of genes stored in the individual
 */
public class Individual<V> implements Comparable<Individual> {

    /**
     * The fitness score of the individual.
     */
    private double fitness;

    /**
     * The value of the individual genes.
     */
    private V genes;

    /**
     * Gets the assigned fitness score for the individual.
     *
     * @return the individual's fitness score
     */
    public double getFitness() {
        return this.fitness;
    }

    /**
     * Assigns a fitness score to the individual.
     *
     * @param fitness the individual's calculated fitness score
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Gets the value of the individual's genes. An individual's genes are used to calculate
     * its fitness score.
     *
     * @return the individual's value
     */
     public V getGenes() {
         return this.genes;
     }

    /**
     * Assigns a value to the individual's genes.
     *
     * @param genes the individual's genes
     */
     public void setGenes(V genes) {
         this.genes = genes;
     }

    /**
     * Compares the current individual to another individual. Sorts individuals in ascending order by fitness score.
     *
     * @param otherIndividual an individual to compare the current to
     * @return -1 if current individual has smaller fitness, 1 if larger, 0 if equal
     */
    @Override
    public int compareTo(Individual otherIndividual) {

        if(this.fitness > otherIndividual.getFitness())
            return 1;
        else if(this.fitness < otherIndividual.getFitness())
            return -1;

        return 0;
    }

}
