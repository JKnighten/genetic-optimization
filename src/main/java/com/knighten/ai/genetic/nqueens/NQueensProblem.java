package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents the n queens problem. Given a n x n sized chess board, find a way to position n queens on the board such
 * that none of the queens are in conflict with one another (ie. they are in the same row or diagonal).
 */
public class NQueensProblem implements IGenOptimizeProblem<NQueensIndividual> {

    /**
     * The number of queens/board size being used; the n in the n queens problem.
     */
    private int n;

    /**
     * Creates a instance of NQueensProblem using the specified value of n.
     *
     * @param n number of queens/board size
     */
    public NQueensProblem(int n) {
        this.n = n;
    }

    /**
     * Creates a population of random NQueensIndividual. This is a collection of random Integer[n] that represents the
     * board of a n queens problem.
     *
     * @param populationSize the size of the population to be created
     * @return the initial random population of NQueensIndividual
     */
    @Override
    public List<com.knighten.ai.genetic.nqueens.NQueensIndividual> generateInitialPopulation(int populationSize) {
        Random random = new Random();

        List<NQueensIndividual> initialPopulation = new ArrayList<>();
        while(initialPopulation.size() != populationSize){
            Integer[] board = new Integer[n];
            for(int column=0; column<n; column++)
                board[column] = random.nextInt(n);

            initialPopulation.add(new NQueensIndividual(board));
        }

        return initialPopulation;
    }

    /**
     * Calculates fitness for each individual in the population. For the NQueensProblem fitness is calculated by finding
     * the number of conflicts that are on the genes/board contained in the NQueensIndividuals. Queens are in conflict
     * if they may take one another (ie. they are in the same row or diagonal). This uses NQueensHelper to calculate the
     * amount of conflict.
     *
     * @param population the population of individuals whose fitness are to be set
     */
    @Override
    public void calculateFitness(List<NQueensIndividual> population) {
        for(Individual individual: population)
            individual.setFitness(NQueensHelper.conflictScore((Integer[]) individual.getGenes()));

        // This sort makes selection() and getBestIndividual() simpler
        Collections.sort(population);
    }

    /**
     * Gets the NQueensIndividual with the lowest fitness score.
     *
     * @param population the population used to search for the best individual
     * @return the NQueensIndividual with the lowest fitness score
     */
    @Override
    public NQueensIndividual getBestIndividual(List<NQueensIndividual> population) {
        return population.get(0);
    }

    /**
     * Selects the selectionPercent percent of best NQueensIndividuals in the population. The best NQueensIndividuals
     * are the ones with the lowest fitness score, which with the least amount of conflicts.
     *
     * @param population the population that the sub-population is selected from
     * @param selectionPercent the percent of best individuals to keep
     * @return the sub-population of best NQueensIndividuals
     */
    @Override
    public List<NQueensIndividual> selection(List<NQueensIndividual> population, double selectionPercent) {
        int amountToRemove = (int) Math.floor((1-selectionPercent) * population.size());

        // Return a modified copy of the original population
        List<NQueensIndividual> selectedPopulation = new ArrayList<>(population);
        for(int i=population.size()-1; i>(population.size()-amountToRemove-1); i--)
            selectedPopulation.remove(i);

        return selectedPopulation;
    }

    /**
     * Generates a new population of NQueensIndividuals by crossing members of the supplied sub-population. Crossing
     * occurs by selecting two random NQueensIndividuals and then combining both of their genes/boards. Genes/boards are
     * combined by selecting a random column which is used as a crossing point. All the values up to this crossing point
     * in the first individual are copied into the into the new individual. Then all values from the crossing point and
     * beyond are are copied from the second individual into the new individual to complete its genes/board.
     *
     * @param subPopulation the sub-population used to generate the new population
     * @param populationSize the desired population size to be returned
     * @return the new population of NQueenIndividuals generated from crossing the sub-population
     */
    @Override
    public List<NQueensIndividual> crossover(List<NQueensIndividual> subPopulation, int populationSize) {
        Random random = new Random();
        ArrayList<NQueensIndividual> newPopulation = new ArrayList<>();

        while(newPopulation.size() != populationSize){
            NQueensIndividual individ1 = subPopulation.get(random.nextInt(subPopulation.size()));
            NQueensIndividual individ2 = subPopulation.get(random.nextInt(subPopulation.size()));

            int crossPoint = random.nextInt(n);
            Integer[] crossedBoard = new Integer[n];
            for(int column=0; column<n; column++)
                crossedBoard[column] = (column<crossPoint) ? individ1.getGenes()[column] : individ2.getGenes()[column];

            newPopulation.add(new NQueensIndividual(crossedBoard));
        }

        return newPopulation;
    }

    /**
     * Randomly mutates the genes of the NQueensIndividuals in the population. This randomly changes position of queens
     * in the individuals' genes/board.
     *
     * @param population the population that will be mutated
     * @param mutationProb the probability that a gene of an individual is mutated
     */
    @Override
    public void mutate(List<NQueensIndividual> population, double mutationProb) {
        Random random = new Random();

        for(NQueensIndividual individual : population)
            for(int column=0; column<n; column++)
                if(random.nextDouble() < mutationProb)
                    individual.getGenes()[column] = random.nextInt(n);

    }


    /**
     * A test execution of the NQueensProblem.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // Genetic Optimization Parameters //
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000,5000, .05, .01);
        params.setTargetValue(0.0);

        // Setup Problem //
        IGenOptimizeProblem problem = new NQueensProblem(128);
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        // Run Optimization //
        long startTime = System.nanoTime();
        List<Individual> optimizationGeneration = optimizer.optimize();
        long endTime = System.nanoTime();
        double duration = (endTime - startTime)/1000000.0;

        // Print Results
        Individual initial = optimizationGeneration.get(0);
        Individual optimized = optimizationGeneration.get(optimizationGeneration.size()-1);
        System.out.println("Generation " + 0 + ": \n" + initial.toString() + " Score - " + initial.getFitness());
        System.out.println("Generation " + optimizationGeneration.size() + ": \n" + optimized.toString() + " Score - "
                + optimized.getFitness());
        System.out.println("Optimization Duration: " + duration + " ms");
        System.out.println("Generations: " + optimizationGeneration.size());
    }
}
