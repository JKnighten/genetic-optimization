package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An abstract class that is the framework for optimizing a one variable real valued function. This contains the
 * necessary methods to perform maximization or minimization. To use only getBestIndividual() and selection() need to
 * be implemented to reflect the type of optimization to perform(minimization or maximization).
 */
public abstract class OptimizeOneVar implements IGenOptimizeProblem<OneVarIndividual> {

    /**
     *  Used to generate random numbers. Allows the use of a seed.
     */
    private Random random;

    /**
     * The smallest x value the search should consider.
     */
    private double minDomain;

    /**
     * The largest x value the search should consider.
     */
    private double maxDomain;

    /**
     * The function being optimized.
     */
    private IOneVariableFunction function;


    /**
     *  Sets the Random object used to generate random numbers.
     *
     * @param random the Random object to use
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Sets the minimum x value to be considered in the search.
     *
     * @param minDomain the smallest x value in search
     */
    public void setMinDomain(double minDomain) {

        // Catch NaN Or Infinity
        if(!Double.isFinite(minDomain))
            throw new IllegalArgumentException("minDomain Cannot Be NaN or Infinite: " + minDomain + " was found");

        // Catch Double.MIN_VALUE
        if(minDomain == Double.MIN_VALUE)
            throw new IllegalArgumentException("minDomain Cannot Double.MIN_VALUE");

        // Catch Double.MAX_VALUE
        if(minDomain == Double.MAX_VALUE)
            throw new IllegalArgumentException("minDomain Cannot Double.MAX_VALUE");

        this.minDomain = minDomain;
    }

    /**
     * Sets the maximum x value to be considered in the search.
     *
     * @param maxDomain the largest x value in search
     */
    public void setMaxDomain(double maxDomain) {

        // Catch NaN Or Infinity
        if(!Double.isFinite(maxDomain))
            throw new IllegalArgumentException("maxDomain Cannot Be NaN or Infinite: " + maxDomain + " was found");

        // Catch Double._VALUE
        if(maxDomain == Double.MIN_VALUE)
            throw new IllegalArgumentException("maxDomain Cannot Double.MIN_VALUE");

        // Catch Double.MAX_VALUE
        if(maxDomain == Double.MAX_VALUE)
            throw new IllegalArgumentException("maxDomain Cannot Double.MAX_VALUE");


        this.maxDomain = maxDomain;
    }

    /**
     * Sets the function being optimized.
     *
     * @param function the function being optimized
     */
    public void setFunction(IOneVariableFunction function) {

        // Catch Null
        if(function == null)
            throw new IllegalArgumentException("function Cannot Be Null");


        this.function = function;
    }

    /**
     * Gets the best individual in the population based upon fitness score(highest/lowest). The individual whose x
     * value creates the smallest/highest function output.
     *
     * @param population the population used to search for the best individual
     * @return the best individual from the population
     */
    @Override
    abstract public OneVarIndividual getBestIndividual(List<OneVarIndividual> population);

    /**
     * Select individuals in the population that will be used to generate next generation's population. The set of
     * individuals whose x values create the smallest/highest function outputs.
     *
     * @param population the population that the sub-population is selected from
     * @param selectionPercent the percent of best individuals to keep
     * @return the sub-population selected from the population
     */
    @Override
    abstract public List<OneVarIndividual> selection(List<OneVarIndividual> population,
                                                     double selectionPercent);
    /**
     * Generates an initial population of individuals. Creates a collection of random x values and puts them into a
     * list of OneVarIndividual.
     *
     * @param populationSize the size of the population to be created
     * @return randomly created population
     */
    @Override
    public List<OneVarIndividual> generateInitialPopulation(int populationSize) {
        return this.random.doubles(populationSize, minDomain, maxDomain)
                .mapToObj(OneVarIndividual::new)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the fitness score for every individual in the population. The fitness in this case is just the value
     * of the function using an individual's x value. Note: this will set the fitness value of all individuals in the
     * provided population and sort the population in ascending order by fitness.
     *
     * @param population the population of individuals whose fitness are to be set
     */
    @Override
    public void calculateFitness(List<OneVarIndividual> population) {
        population.stream().forEach(individual -> individual.setFitness(function.getFuncValue(individual.getGenes())));
        Collections.sort(population); // Sort Now To Make selection() More Efficient
    }

    /**
     * Uses the provided sub-population to generate the next generation's population. Random guesses of x values are
     * selected and crossed. Values are crossed by taking the average of their x values.
     *
     * @param subPopulation the sub-population used to generate the new population
     * @param populationSize the desired population size to be returned
     * @return population created from crossing the individuals in the sub-population
     */
    @Override
    public List<OneVarIndividual> crossover(List<OneVarIndividual> subPopulation, int populationSize) {
        double[] genes= this.random.ints(populationSize*2,0, subPopulation.size())
                .mapToDouble(i -> subPopulation.get(i).getGenes())
                .toArray();

        return IntStream.range(0, populationSize)
                .mapToDouble(i -> (genes[i] + genes[i+populationSize])/2)
                .mapToObj(OneVarIndividual::new)
                .collect(Collectors.toList());
    }

    /**
     * Randomly mutate individuals in the supplied population. We select random OneVarIndividuals and change their x
     * values. Note: this will change the genes of the individuals in the supplied population.
     *
     * @param population the population that will be mutated
     * @param mutationProb the probability that a gene of an individual is mutated
     */
    @Override
    public void mutate(List<OneVarIndividual> population, double mutationProb) {
        double[] randomMut = this.random.doubles(population.size()).toArray();
        double[] randomX = this.random.doubles(population.size(), minDomain, maxDomain).toArray();

        IntStream.range(0, population.size())
                .filter(i -> randomMut[i] < mutationProb)
                .forEach(i -> population.get(i).setGenes(randomX[i]));
    }
}
