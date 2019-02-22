package com.knighten.ai.genetic.stringmatch;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents the string matching problem to be solved by genetic optimization. Starting with random strings attempt to
 * generate the target string.
 */
public class StringMatchProblem implements IGenOptimizeProblem<StringMatchProblem.StringIndividual> {

    /**
     * The target string that the genetic algorithm is trying to generate.
     */
    private String targetString;

    /**
     * Creates a instance of StringMatchProblem containing the target string trying to be obtained.
     *
     * @param targetString the string trying to be found by the genetic algorithm
     */
    public StringMatchProblem(String targetString) {
        this.targetString = targetString;
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
        List<StringMatchProblem.StringIndividual> initialPopulation = new ArrayList<>();
        while(initialPopulation.size() != populationSize)
            initialPopulation.add(new StringIndividual(RandomTextHelper.generateString(targetString.length())));

        return initialPopulation;
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
        for(StringIndividual individual: population) {
            double fitness = 0.0;
            for(int i = 0; i<individual.getGenes().length(); i++)
                fitness += Math.abs(individual.getGenes().charAt(i) - targetString.charAt(i));

            individual.setFitness(fitness);
        }

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

        // Return a modified copy of the original population
        List<StringMatchProblem.StringIndividual> selectedPopulation = new ArrayList<>(population);
        for(int i=population.size()-1; i>(population.size()-amountToRemove-1); i--)
            selectedPopulation.remove(i);

        return selectedPopulation;
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
        Random random = new Random();
        ArrayList<StringIndividual> newPopulation = new ArrayList<>();

        while(newPopulation.size() != populationSize){
            StringIndividual individual1 = subPopulation.get(random.nextInt(subPopulation.size()));
            StringIndividual individual2 = subPopulation.get(random.nextInt(subPopulation.size()));

            int crossOverPoint = random.nextInt(targetString.length());
            String leftHalf = individual1.getGenes().substring(0, crossOverPoint);
            String rightHalf = individual2.getGenes().substring(crossOverPoint);
            StringIndividual crossedIndividual = new StringIndividual( leftHalf + rightHalf);

            newPopulation.add(crossedIndividual);
        }

        return newPopulation;
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
        Random random = new Random();

        for(StringIndividual individual: population){
            char[] valAsCharArray = individual.getGenes().toCharArray();

            // Possible for character to be replaced with same character
            for(int i=0; i<valAsCharArray.length; i++)
                if(random.nextDouble() < mutationProb)
                    valAsCharArray[i] = RandomTextHelper.generateChar();

            String mutatedString = new String(valAsCharArray);
            individual.setGenes(mutatedString);
        }
    }

    /**
     * Represents an individual in the string matching problem. Stores a string and a fitness score.
     */
    public class StringIndividual extends Individual<String> {

        /**
         * Creates a individual for the string matching problem. Stores the supplied string as its genes.
         *
         * @param stringValue the string contained in the individual
         */
        public StringIndividual(String stringValue){
            this.setGenes(stringValue);
        }

        /**
         * Returns the string representation of the individuals genes. Since the individual genes are a string, they are
         * directly returned.
         *
         * @return the individuals value
         */
        @Override
        public String toString(){ return this.getGenes(); }

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
        IGenOptimizeProblem<StringIndividual> problem = new StringMatchProblem("Hello String Matching");
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



