package com.knighten.ai.genetic.interfaces;

import com.knighten.ai.genetic.Individual;
import java.util.List;

/**
 * Represents a problem that can be optimized using the genetic algorithm.
 *
 * @param <T> a type of individual used to represent a population in the problem
 */
public interface IGenOptimizeProblem <T extends Individual> {

    /**
     * Generates an initial population of individuals.
     *
     * @param populationSize the size of the population to be created
     * @return randomly created population
     */
    List<T> generateInitialPopulation(int populationSize);

    /**
     * Calculates the fitness score for every individual in the population. Note: this will set the
     * fitness value of all individuals in the provided population.
     *
     * @param population the population of individuals whose fitness are to be set
     */
    void calculateFitness(List<T> population);

    /**
     * Gets the best individual in the population based upon fitness score(highest/lowest).
     *
     * @param population the population used to search for the best individual
     * @return the best individual in the population
     */
    T getBestIndividual(List<T> population);

    /**
     * Select individuals in the population that will be used to generate next generation's population.
     *
     * @param population the population that the sub-population is selected from
     * @param selectionPercent the percent of best individuals to keep
     * @return the sub-population selected from the population
     */
    List<T> selection(List<T> population, double selectionPercent);

    /**
     * Uses the provided sub-population to generate the next  generation's population.
     *
     * @param subPopulation the sub-population used to generate the new population
     * @param populationSize the desired population size to be returned
     * @return population created from crossing the individuals in the sub-population
     */
    List<T> crossover(List<T> subPopulation, int populationSize);

    /**
     * Randomly mutate individuals in the supplied population. Note: this will change the values
     * of the individuals in the supplied population.
     *
     * @param population the population that will be mutated
     * @param mutationProb the probability that a gene of an individual is mutated
     */
    void mutate(List<T> population, double mutationProb);

}
