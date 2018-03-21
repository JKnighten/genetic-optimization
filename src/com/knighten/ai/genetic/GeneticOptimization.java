package com.knighten.ai.genetic;

import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.ArrayList;
import java.util.List;

public class GeneticOptimization {


    private IGenOptimizeProblem problem;
    private int maxGenerations;
    private double selectionPercent;
    private double mutationProb;
    private int populationSize;

    public GeneticOptimization(IGenOptimizeProblem problem, int maxGenerations, int populationSize, double selectionPercent, double mutationProb){
        this.problem = problem;
        this.maxGenerations = maxGenerations;
        this.selectionPercent = selectionPercent;
        this.mutationProb = mutationProb;
        this.populationSize = populationSize;
    }

    public List<Individual> optimize(){

        // Generate Initial Population //
        List<Individual> population = problem.generateInitialPopulation(populationSize);

        // Population Fitness //
        problem.calculateFitness(population);

        // Store Best Individual For Each Generation //
        List<Individual> generations = new ArrayList<>();
        generations.add(problem.getBestIndividual(population));

        // Generations //
        for(int i=0; i<maxGenerations; i++){

            // Selection //
            List<Individual> selectedPopulation = problem.selection(population, selectionPercent);

            // Cross Over //
            List<Individual> crossedPopulation = problem.crossover(selectedPopulation, populationSize);

            // Mutation //
            problem.mutate(crossedPopulation, mutationProb);

            // Population Fitness //
            problem.calculateFitness(crossedPopulation);

            Individual topIndividual = problem.getBestIndividual(crossedPopulation);
            generations.add(topIndividual);

            population = crossedPopulation;

            //TODO - Make Stopping Condition More Flexible
            if(topIndividual.getFitness() == 0.0)
                break;
        }

        return generations;

    }



}
