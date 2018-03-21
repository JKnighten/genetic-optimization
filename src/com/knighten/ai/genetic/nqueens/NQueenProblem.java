package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NQueenProblem implements IGenOptimizeProblem<NQueensIndividual> {


    private int n;

    public NQueenProblem(int n){
        this.n = n;
    }


    @Override
    public List<NQueensIndividual> generateInitialPopulation(int populationSize) {
        List<NQueensIndividual> initalPopulation = new ArrayList<>();

        Random random = new Random();

        for(int i=0; i<populationSize; i++){
            Integer[] board = new Integer[n];
            for(int j=0; j<n; j++){
                board[j] = random.nextInt(n);
            }

            initalPopulation.add(new NQueensIndividual(board));

        }

        return initalPopulation;
    }

    @Override
    public void calculateFitness(List<NQueensIndividual> population) {
        for(Individual individual: population)
            individual.setFitness(NQueenHelper.conflictScore((Integer[]) individual.getValue()));

        Collections.sort(population);
    }




    @Override
    public NQueensIndividual getBestIndividual(List<NQueensIndividual> population) {
        return population.get(0);
    }

    @Override
    public List<NQueensIndividual> selection(List<NQueensIndividual> population, double selectionPercent) {
        int howManyToRemove = (int) Math.floor((1-selectionPercent) * population.size());
        List<NQueensIndividual> selectedPopulation = new ArrayList<>(population);
        for(int i=population.size()-1; i> (population.size()-howManyToRemove-1); i--)
            selectedPopulation.remove(i);

        return selectedPopulation;
    }

    @Override
    public List<NQueensIndividual> crossover(List<NQueensIndividual> selectedPopulation, int populationSize) {

        ArrayList<NQueensIndividual> newPopulation = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<populationSize; i++){
            NQueensIndividual individual1 = selectedPopulation.get(random.nextInt(selectedPopulation.size()));
            NQueensIndividual individual2 = selectedPopulation.get(random.nextInt(selectedPopulation.size()));

            int crossOverPoint = random.nextInt(n);
            Integer[] crossedIndividualBoard = new Integer[n];
            for(int j=0; j<n; j++){

                if( j< crossOverPoint)
                    crossedIndividualBoard[j] = individual1.getValue()[j];
                else
                    crossedIndividualBoard[j] = individual2.getValue()[j];
            }


            NQueensIndividual crossedIndividual = new NQueensIndividual(crossedIndividualBoard);

            newPopulation.add(crossedIndividual);
        }

        return newPopulation;

    }

    @Override
    public void mutate(List<NQueensIndividual> population, double mutationProb) {

        Random random = new Random();

        for(int j=0; j< population.size(); j++) {

            NQueensIndividual current = population.get(j);

            for(int i=0; i<n; i++)
                if(random.nextDouble() < mutationProb)
                    current.getValue()[i] = random.nextInt(n);

        }

    }




    public static void main(String[] args){

        // Genetic Optimization Parameters //
        int maxGenerations = 5000;
        int populationSize = 100;
        double selectionPercent = 0.2;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem problem = new NQueenProblem(32);

        GeneticOptimization optimizer = new GeneticOptimization(problem, maxGenerations, populationSize ,selectionPercent, mutationPercent);
        long startTime = System.nanoTime();
        List<Individual> optimizationGeneration = optimizer.optimize();
        long endTime = System.nanoTime();



        Individual initial = optimizationGeneration.get(0);
        Individual optimized = optimizationGeneration.get(optimizationGeneration.size()-1);

        System.out.println("Generation " + 0 + ": \n" + initial.toString() + " Score - " + initial.getFitness());
        System.out.println("Generation " + optimizationGeneration.size() + ": \n" + optimized.toString() + " Score - " + optimized.getFitness());

        double duration = (endTime - startTime)/1000000.0;

        System.out.println("Optimization Duration: " + duration + " ms");
        System.out.println("Generations: " + optimizationGeneration.size());
    }
}
