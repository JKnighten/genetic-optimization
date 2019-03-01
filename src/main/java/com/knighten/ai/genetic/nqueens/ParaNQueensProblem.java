package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Represents the n queens problem that is parallelized. Given a n x n sized chess board, find a way to position n
 * queens on the board such that none of the queens are in conflict with one another (ie. they are in the same row or
 * diagonal). Parallelization is implemented by using Java 1.8 streams. This implementation is meant to be used when
 * the population size or n is very large.
 */
public class ParaNQueensProblem extends BaseNQueensProblem {

    /**
     * The number of queens/board size being used; the n in the n queens problem.
     */
    private int n;

    /**
     *  Used to generate random numbers. Allows the use of a seed.
     */
    private Random random;

    /**
     * Creates a instance of ParaNQueensProblem using the specified value of n.
     *
     * @param n number of queens/board size
     */
    public ParaNQueensProblem(int n, Random random) {

        if(n <= 3)
            throw new IllegalArgumentException("N Must Be Greater Than 3");

        if(random == null)
            throw new IllegalArgumentException("Random Object Cannot Be Null");

        this.n = n;
        this.random = random;
    }

    /**
     * Creates a population of random NQueensIndividual. This is a collection of random Integer[n] that represents the
     * board of a n queens problem.
     *
     * @param populationSize the size of the population to be created
     * @return the initial random population of NQueensIndividual
     */
    @Override
    public List<NQueensIndividual> generateInitialPopulation(int populationSize) {
        return IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> this.random.ints(n, 0, n).boxed().toArray(Integer[]::new))
                .map(NQueensIndividual::new)
                .collect(toList());
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
        population.parallelStream()
                .forEach(individual -> individual.setFitness(this.conflictScore(individual)));

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
     * @return the sub-population of best NQueesnIndividuals
     */
    @Override
    public List<NQueensIndividual> selection(List<NQueensIndividual> population, double selectionPercent) {
        int amountToRemove = (int) Math.floor((1-selectionPercent) * population.size());

        return IntStream.rangeClosed(0, population.size()-amountToRemove-1)
                .parallel()
                .mapToObj(population::get)
                .collect(Collectors.toList());
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
        List<NQueensIndividual> randomPairs = this.random.ints(2*populationSize, 0, subPopulation.size())
                .mapToObj(subPopulation::get)
                .collect(toList());

        int[] crossPoints = this.random.ints(populationSize, 0, this.n).toArray();

        return IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> crossIndividuals(randomPairs.get(i), randomPairs.get(i+populationSize), crossPoints[i]))
                .map(NQueensIndividual::new)
                .collect(toList());
    }

    /**
     * Performs the crossing mentioned above. This is called in parallel.
     *
     * @param individ1 individual were first half of genes is taken from
     * @param individ2 individual were second half of genes is taken from
     * @param crossPoint the point which genes are crossed
     * @return a new NQueensIndividual created by individuals in selectedPopulation
     */
    private Integer[] crossIndividuals(NQueensIndividual individ1, NQueensIndividual individ2, int crossPoint) {
        Integer[] crossedBoard = new Integer[this.n];
        for(int column=0; column<this.n; column++)
            crossedBoard[column] = (column<crossPoint) ? individ1.getGenes()[column] : individ2.getGenes()[column];

        return crossedBoard;
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
        population.stream()
                .parallel()
                .forEach((individual) -> {
                    double[] mutationChance = this.random.doubles(this.n).toArray();
                    int[] randQueenPositions = this.random.ints(this.n, 0, this.n).toArray();

                    Integer[] genes = individual.getGenes();

                    IntStream.range(0, this.n)
                            .filter((i) -> mutationChance[i] < mutationProb)
                            .forEach((column) -> genes[column] = randQueenPositions[column]);

                    individual.setGenes(genes);
                });
    }

    /**
     * A test execution of the ParaNQueensProblem.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // Genetic Optimization Parameters //
        GeneticOptimizationParams params = new GeneticOptimizationParams(10000, 1000,.05, .01);
        params.setTargetValue(0.0);


        // Setup Problem //
        IGenOptimizeProblem problem = new ParaNQueensProblem(12, new Random(1L));
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
