package com.knighten.ai.genetic;

/**
 * Represents problems that can bed optimized using the genetic algorithm.
 */
public interface IGenOptimizeProblem {

    /**
     * Generates an initial population of individuals.
     */
    void generateInitialPopulation();

    /**
     * Calculates the fitness score for every individual in the population.
     */
    void calculateFitness();

    /**
     * Gets the best individual in the population based upon fitness score(highest/lowest).
     *
     * @return the best individual in the population
     */
    Individual getBestIndividual();

    /**
     * Select what individuals in the population will be used to generate next generation's population.
     */
    void selection();

    /**
     * Using the individuals selected in the selection phase, generate the new generation's population.
     */
    void crossover();

    /**
     * Randomly mutate individual's in the new generation's population.
     */
    void mutate();

}
