package com.knighten.ai.genetic.stringmatch;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents the string matching problem to be solved by genetic optimization. Starting with random strings attempt to
 * generate the target string.
 */
public class StringMatchProblem implements IGenOptimizeProblem<StringIndividual> {

    /**
     * The target string that the genetic algorithm is trying to generate.
     */
    private String targetString;


    /**
     *  Used to generate random numbers. Allows the use of a seed.
     */
    private Random random;

    /**
     *  Used to generate text data that is valid in the problem.
     */
    private RandomTextHelper randomValidText;

    /**
     * Creates a instance of StringMatchProblem containing the target string trying to be obtained.
     *
     * @param targetString the string trying to be found by the genetic algorithm
     * @param random object used to generate random numbers for the problem
     */
    public StringMatchProblem(String targetString, Random random) {
        this.targetString = targetString;
        this.random = random;
        this.randomValidText = new RandomTextHelper(random);
    }

    /**
     * Creates a population of random StringIndividuals. This is a collection of random strings created using
     * RandomTextHelper.
     *
     * @param populationSize the size of the population to be created
     * @return the initial random population of StringIndividual
     */
    @Override
    public List<StringIndividual> generateInitialPopulation(int populationSize) {

        return Collections.nCopies(populationSize, targetString.length())
                .stream()
                .map(randomValidText::generateString)
                .map(StringIndividual::new)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the fitness for each individual in the population. For the StringMatchProblem fitness is calculated
     * by finding the sum of the absolute differences between the characters in the individual's string and the target
     * string. The difference is defined by the distance using the unicode integer representation of the characters.
     *
     * @param population the population of individuals whose fitness scores are to be set
     */
    @Override
    public void calculateFitness(List<StringIndividual> population) {
        population.stream()
                .forEach((individual) -> {
                    // Find The Difference Between Each Character In The Current Individual and Target Gene
                    // Sum All Differences For Final Score
                    double score = IntStream.range(0, individual.getGenes().length())
                            .map((i) -> Math.abs(individual.getGenes().charAt(i) - targetString.charAt(i)))
                            .sum();
                    individual.setFitness(score);
                });

        // This sort makes selection() and getBestIndividual() simpler
        Collections.sort(population);
    }

    /**
     * Gets the best individual in the population. In this case the individual with the lowest fitness score, which
     * is the individual that is most similar to the target string.
     *
     * @param population the population used to search for the best individual
     * @return the StringIndividual with the lowest fitness
     */
    @Override
    public StringIndividual getBestIndividual(List<StringIndividual> population) {
        return population.get(0);
    }

    /**
     * Selects the selectionPercent percent of best StringIndividuals in the population. The best StringIndividuals are
     * the ones with the lowest fitness score, which are those closest to the target string.
     *
     * @param population the population that is the sub-population is selected from
     * @param selectionPercent the percent of best individuals to keep
     * @return the sub-population of best StringIndividuals
     */
    @Override
    public List<StringIndividual> selection(List<StringIndividual> population, double selectionPercent) {
        int amountToRemove = (int) Math.floor((1-selectionPercent) * population.size());

        return IntStream.rangeClosed(0, population.size()-amountToRemove-1)
                .mapToObj(population::get)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new population of StringIndividuals by crossing StringIndividuals in the supplied sub-population. Two
     * StringIndividuals are randomly selected, then portions of their strings(genes) are swapped to create a new
     * StringIndividual.
     *
     * @param subPopulation the sub-population used to generate the new population
     * @param populationSize the desired population size to be returned
     * @return the new population of StringIndividual created from the sub-population
     */
    @Override
    public List<StringIndividual> crossover(List<StringIndividual> subPopulation, int populationSize) {

        // Select Random Individuals To Cross With Each Other
        // We Select 2 Times The Desired Population Size To Create Pairs
        List<StringIndividual> individuals = this.random.ints(populationSize*2,0, subPopulation.size())
                .mapToObj(subPopulation::get)
                .collect(Collectors.toList());

        // For Each Pair We Pick A Split Point To Separate Their Genes
        int[] splitPoints = this.random.ints(populationSize,0, targetString.length()).toArray();

        // Create The New StringIndividuals
        return IntStream.range(0, populationSize)
                .mapToObj((i) -> {
                    String gene1Contribution = individuals.get(i).getGenes().substring(0, splitPoints[i]);
                    String gene2Contribution = individuals.get(i+populationSize).getGenes().substring(splitPoints[i]);
                    return gene1Contribution + gene2Contribution;
                })
                .map(StringIndividual::new)
                .collect(Collectors.toList());
    }

    /**
     * Mutates the StringIndividuals in a population. There is a mutationProb percent chance that a character in a
     * StringIndividual's string will be mutated. If a character is selected for mutation then that character is
     * replaced by a randomly selected character.
     *
     * @param population the population that will be mutated
     * @param mutationProb the probability that a gene of an individual is mutated
     */
    @Override
    public void mutate(List<StringIndividual> population, double mutationProb) {
        population.stream()
                .forEach((individual) -> {
                    double[] mutationChance = this.random.doubles(targetString.length()).toArray();
                    char[] genesAsArray = individual.getGenes().toCharArray();
                    IntStream.range(0, targetString.length())
                            .filter((i) -> mutationChance[i] < mutationProb)
                            .forEach((i) -> genesAsArray[i] = this.randomValidText.generateChar());

                    individual.setGenes(new String(genesAsArray));
                });
    }

    /**
     * A test execution of the StringMatchProblem.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // Genetic Optimization Parameters //
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 2000,.2, .01);
        params.setTargetValue(0.0);

        // Setup Problem //
        IGenOptimizeProblem<StringIndividual> problem = new StringMatchProblem("Hello String Matching",
                new Random());
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        // Run Optimization //
        long startTime = System.nanoTime();
        List<Individual> optimizationGenerations = optimizer.optimize();
        long endTime = System.nanoTime();

        double duration = (endTime - startTime)/1000000.0;

        // Print Results
        for(int i=1; i<optimizationGenerations.size(); i++) {
            if(optimizationGenerations.get(i).getFitness() < optimizationGenerations.get(i-1).getFitness() ){
                String individualString = optimizationGenerations.get(i).toString();
                double fitness = optimizationGenerations.get(i).getFitness();
                System.out.println("Generation " + i + ": - Score " + fitness + "\n" + individualString );
            }
        }

        System.out.println("Optimization Duration: " + duration + " ms");
        System.out.println("Number of Generations: " + optimizationGenerations.size());
    }

}



