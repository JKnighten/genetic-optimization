package com.knighten.ai.genetic;

//TODO - Update Javadocs

/**
 * Represents an individual in the population used in the genetic optimization algorithm.
 *
 * @param <V> represents the type of value stored in the individual
 */
public class Individual<V> implements Comparable<Individual> {

    /**
     * The fitness score of the individual.
     */
    private double fitness;

    /**
     * The value of the individual.
     */
    private V value;

    /**
     * Gets the assigned fitness score for the individual.
     *
     * @return the individual's fitness score
     */
    public double getFitness() { return this.fitness; }

    /**
     * Assigns a fitness score to the individual.
     *
     * @param fitness the individual's calculated fitness score
     */
    public void setFitness(double fitness) { this.fitness = fitness; }

    /**
     * Gets the value of the individual. An individual's value is used to calculate
     * its fitness score.
     *
     * @return the individual's value
     */
     public V getValue() { return this.value; }

    /**
     * Assigns a value to the individual.
     *
     * @param value the individual's value
     */
     public void setValue(V value) { this.value = value; }

    /**
     * Compares the current individual to another individual.
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
