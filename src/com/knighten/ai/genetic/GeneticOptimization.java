package com.knighten.ai.genetic;

import java.util.ArrayList;
import java.util.List;

public class GeneticOptimization {


    private IGenOptimizeProblem problem;
    private int maxGenerations;

    public GeneticOptimization(IGenOptimizeProblem problem, int maxGenerations){
        this.problem = problem;
        this.maxGenerations = maxGenerations;
    }

    public List<Individual> optimize(){

        // Generate Initial Population //
        problem.generateInitialPopulation();

        // Population Fitness //
        problem.calculateFitness();

        // Store Best Individual For Each Generation //
        List<Individual> generations = new ArrayList<>();
        generations.add(problem.getBestIndividual());

        // Generations //
        for(int i=0; i<maxGenerations; i++){

            // Selection //
            problem.selection();

            // Cross Over //
            problem.crossover();

            // Mutation //
            problem.mutate();

            // Population Fitness //
            problem.calculateFitness();

            Individual topIndividual = problem.getBestIndividual();
            generations.add(topIndividual);


            if(problem.getBestIndividual().getFitness() == 0.0)
                break;
        }

        return generations;

    }


}
