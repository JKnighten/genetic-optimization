package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.Individual;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimizeOneVarFunction implements IGenOptimizeProblem<OneVarFunctionIndividual> {

    private double minDomain;
    private double maxDomain;
    private IOneVariableFunction function;
    private Random random;

    public MinimizeOneVarFunction(double minDomain, double maxDomain, IOneVariableFunction function, Random random) {
        this.minDomain = minDomain;
        this.maxDomain = maxDomain;
        this.function = function;
        this.random = random;
    }

    @Override
    public List<OneVarFunctionIndividual> generateInitialPopulation(int populationSize) {
        return this.random.doubles(populationSize, minDomain, maxDomain)
                .mapToObj(OneVarFunctionIndividual::new)
                .collect(Collectors.toList());
    }

    @Override
    public void calculateFitness(List<OneVarFunctionIndividual> population) {
        population.stream().forEach(individual -> individual.setFitness(function.getFuncValue(individual.getGenes())));
        Collections.sort(population); // Sort Now To Make selection() More Efficient
    }

    @Override
    public OneVarFunctionIndividual getBestIndividual(List<OneVarFunctionIndividual> population) {
        return population.get(0); // Grab first since we want to minimize
    }

    @Override
    public List<OneVarFunctionIndividual> selection(List<OneVarFunctionIndividual> population, double selectionPercent) {
        int amountToRemove = (int) Math.floor((1-selectionPercent) * population.size());

        return IntStream.rangeClosed(0, population.size()-amountToRemove-1)
                .mapToObj(population::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<OneVarFunctionIndividual> crossover(List<OneVarFunctionIndividual> subPopulation, int populationSize) {
        double[] genes= this.random.ints(populationSize*2,0, subPopulation.size())
                .mapToDouble(i -> subPopulation.get(i).getGenes())
                .toArray();

        return IntStream.range(0, populationSize)
                .mapToDouble(i -> (genes[i] + genes[i+populationSize])/2)
                .mapToObj(OneVarFunctionIndividual::new)
                .collect(Collectors.toList());
    }

    @Override
    public void mutate(List<OneVarFunctionIndividual> population, double mutationProb) {
        double[] randomMut =  this.random.doubles(population.size()).toArray();
        double[] randomX = this.random.doubles(population.size(), minDomain, maxDomain).toArray();

        IntStream.range(0, population.size())
                .filter(i -> randomMut[i] < mutationProb)
                .forEach(i -> population.get(i).setGenes(randomX[i]));
    }

    public static void main(String[] args) {

        // Genetic Optimization Parameters //
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000,10000, .15, .01);
        params.setTargetValue(0.0);

        // Setup Problem //
        IGenOptimizeProblem problem = new MinimizeOneVarFunction(-10.0,
                10.0,
                (x) -> Math.pow(x, 2),
                new Random(123));
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
