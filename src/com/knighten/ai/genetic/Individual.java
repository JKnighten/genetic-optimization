package com.knighten.ai.genetic;

/**
 * Represents an individual of the population used in the genetic optimization algorithm.
 *
 * @param <T> represents the value stored in the individual
 */
public class Individual<T> implements Comparable<Individual> {

    /**
     * The fitness score of the individual.
     */
    private double fitness;

    /**
     * The value of the individual.
     */
    private T value;

    /**
     * Gets the assigned fitness score for the individual.
     *
     * @return the individual's fitness score
     */
    public double getFitness(){ return this.fitness; }

    /**
     * Assigns a value to the individual's fitness score.
     *
     * @param fitness the individual's calculated fitness score
     */
    public void setFitness(double fitness){ this.fitness = fitness; }

    /**
     * Gets the individuals value. An individual's value is used to calculate
     * its fitness score.
     *
     * @return the individual's value
     */
     public T getValue(){ return this.value; }

    /**
     * Assigns the individual's its value.
     *
     * @param value the individual's value
     */
     public void setValue(T value){ this.value = value; }

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
