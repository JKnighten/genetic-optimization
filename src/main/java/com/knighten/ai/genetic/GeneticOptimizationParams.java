package com.knighten.ai.genetic;

/**
 * Contains the parameters used by GeneticOptimization.
 */
public class GeneticOptimizationParams {

    /**
     * The max number of generations created during optimization.
     */
    private int maxGenerations;

    /**
     * The percentage of best individuals kept before crossover phase.
     */
    private double selectionPercent;

    /**
     * The probability that a gene of an individual will be mutated.
     */
    private double mutationProb;

    /**
     * The size of the population of individuals used in optimization.
     */
    private int populationSize;

    /**
     * A value used to stop optimization early. Typically it is a value that represents the best achievable fitness
     * score. Optimization terminates when a generation is made whose's best individual has a fitness score equal to
     * this value.
     */
    private double targetValue = Integer.MAX_VALUE;


    /**
     * Creates an instance of GeneticOptimizationParams using the supplied optimization parameters.
     *
     * @param populationSize the size of population of individuals
     * @param maxGenerations the maximum number of generations created
     * @param selectionPercent percentage of best individuals kept during selection
     * @param mutationProb probability a gene is mutated
     */
    public GeneticOptimizationParams(int populationSize, int maxGenerations , double selectionPercent,
                                     double mutationProb) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.selectionPercent = selectionPercent;
        this.mutationProb = mutationProb;
    }

    /**
     * Returns the size of the populations used in the optimization.
     *
     * @return the number of individuals in the population
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * Returns the percentage of individuals maintained before performing crossover.
     *
     * @return the percentage of best individuals kept during selection
     */
    public double getSelectionPercent() {
        return selectionPercent;
    }

    /**
     * Returns the portability used to determine if a gene of an individual will be mutated.
     *
     * @return probability a gene is mutated
     */
    public double getMutationProb() {
        return mutationProb;
    }

    /**
     * Returns the max number of generates that the optimization is allowed to create.
     *
     * @return the maximum number of generations created
     */
    public int getMaxGenerations() {
        return maxGenerations;
    }

    /**
     * Returns the fitness score value that will terminate the optimization.
     *
     * @return the termination fitness score value
     */
    public double getTargetValue() {
        return targetValue;
    }

    /**
     * Sets the fitness score value that will terminate the optimization.
     *
     * @param targetValue the termination fitness score value
     */
    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

}
