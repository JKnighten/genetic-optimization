package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.Individual;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MaximizeOneVar extends OptimizeOneVar {

    public MaximizeOneVar(double minDomain, double maxDomain, IOneVariableFunction function, Random random) {
        this.setMinDomain(minDomain);
        this.setMaxDomain(maxDomain);
        this.setFunction(function);
        this.setRandom(random);
    }

    @Override
    public OneVarIndividual getBestIndividual(List<OneVarIndividual> population) {
        return population.get(population.size()-1);
    }

    @Override
    public List<OneVarIndividual> selection(List<OneVarIndividual> population, double selectionPercent) {
        int amountToRemove = (int) Math.floor((1-selectionPercent) * population.size());

        return IntStream.rangeClosed(amountToRemove, population.size()-1)
                .mapToObj(population::get)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {

        // Genetic Optimization Parameters //
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000,10000, .15, .01);
        params.setTargetValue(0.0);

        // Setup Problem //
        IGenOptimizeProblem problem = new MaximizeOneVar(-10.0,
                10.0,
                (x) -> -Math.pow(x, 2),
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
