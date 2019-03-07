package com.knighten.ai.genetic;

import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Performs genetic optimization on a given problems. Problems must implement IGenOptimizeProblem and use individuals
 * that extend the Individual abstract class.
 */
public class GeneticOptimization {

    /**
     * The problem being optimized.
     */
    private IGenOptimizeProblem<Individual> problem;

    /**
     * Parameters used by the optimizer.
     */
    private GeneticOptimizationParams params;

    /**
     * Creates and instance of GeneticOptimization solving the supplied problems using the supplied parameters.
     *
     * @param problem the problem to be optimized
     * @param params  optimization parameters
     */
    public GeneticOptimization(IGenOptimizeProblem problem, GeneticOptimizationParams params) {

        if (problem == null)
            throw new IllegalArgumentException("The Problem Object Cannot Be null");

        if (params == null)
            throw new IllegalArgumentException("Optimization Parameters Cannot Be null");

        this.problem = problem;
        this.params = params;
    }

    /**
     * Starts the optimization process and returns a list of best individuals created.
     *
     * @return a list of each top individual in each generation
     */
    public List<Individual> optimize() {
        List<Individual> bestInGenerations = new ArrayList<>();

        List<Individual> population = problem.generateInitialPopulation(params.getPopulationSize());
        problem.calculateFitness(population);
        bestInGenerations.add(problem.getBestIndividual(population));

        // Start Generations
        while (bestInGenerations.size() - 1 != params.getMaxGenerations()) {
            List<Individual> selectedPopulation = problem.selection(population, params.getSelectionPercent());
            List<Individual> crossedPopulation = problem.crossover(selectedPopulation, params.getPopulationSize());
            problem.mutate(crossedPopulation, params.getMutationProb());
            problem.calculateFitness(crossedPopulation);
            bestInGenerations.add(problem.getBestIndividual(crossedPopulation));

            population = crossedPopulation;

            if (bestInGenerations.get(bestInGenerations.size() - 1).getFitness() == params.getTargetValue())
                return bestInGenerations;
        }

        return bestInGenerations;
    }

}
