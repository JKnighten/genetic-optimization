package com.knighten.ai.genetic.nqueens;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ParaNQueenProblem implements IGenOptimizeProblem<NQueensIndividual> {


    private int n;
    private int populationSize;

    public ParaNQueenProblem(int n, int populationSize){
        this.n = n;
        this.populationSize = populationSize;
    }


    @Override
    public List<NQueensIndividual> generateInitialPopulation() {
        List<NQueensIndividual > initialPopulation  = IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> new NQueensIndividual(randomBoard(n)))
                .collect(toList());

        return initialPopulation;
    }

    private Integer[] randomBoard(int n){
        Random random = new Random();

        Integer[] board = new Integer[n];
        for(int j=0; j<n; j++){
            board[j] = random.nextInt(n);
        }

        return board;
    }

    @Override
    public void calculateFitness(List<NQueensIndividual> population) {

        population.parallelStream()
                .forEach(individual -> individual.setFitness(NQueenHelper.conflictScore(individual.getValue())));

        Collections.sort(population);
    }




    @Override
    public NQueensIndividual getBestIndividual(List<NQueensIndividual> population) {
        return Collections.min(population);
    }

    @Override
    public List<NQueensIndividual> selection(List<NQueensIndividual> population, double selectionPercent) {
        int howManyToRemove = (int) Math.floor((1-selectionPercent) * populationSize);
        List<NQueensIndividual> selectedPopulation = new ArrayList<>(population);
        for(int i=populationSize-1; i> (populationSize-howManyToRemove-1); i--)
            selectedPopulation.remove(i);

        return selectedPopulation;
    }

    @Override
    public List<NQueensIndividual> crossover(List<NQueensIndividual> selectedPopulation) {

        List<NQueensIndividual> newPopulation = IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> crossIndividuals(selectedPopulation))
                .collect(toList());

        return newPopulation;

    }

    private NQueensIndividual crossIndividuals(List<NQueensIndividual> selectedPopulation){
        Random random = new Random();
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

        return new NQueensIndividual(crossedIndividualBoard);
    }

    @Override
    public void mutate(List<NQueensIndividual> population, double mutationProb) {
        population.parallelStream()
                .forEach(individual -> randomMutation(individual, mutationProb));

    }

    private void randomMutation(NQueensIndividual current, double mutationProb){
        Random random = new Random();
        for(int i=0; i<n; i++)
            if(random.nextDouble() < mutationProb)
                current.getValue()[i] = random.nextInt(n);
    }



    public static void main(String[] args){

        // Genetic Optimization Parameters //
        int maxGenerations = 2000;
        int populationSize = 10000;
        double selectionPercent = 0.2;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem problem = new ParaNQueenProblem(64, populationSize);

        GeneticOptimization optimizer = new GeneticOptimization(problem, maxGenerations, selectionPercent, mutationPercent);
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
