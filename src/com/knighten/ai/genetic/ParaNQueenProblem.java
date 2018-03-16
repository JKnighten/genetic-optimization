package com.knighten.ai.genetic;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ParaNQueenProblem implements IGenOptimizeProblem {

    private List<NQueensIndividual> population;

    private int n;
    private int populationSize;
    private  double selectionPercent;
    private  double mutationPercent;

    public ParaNQueenProblem(int n, int populationSize, double selectionPercent, double mutationPercent){
        this.n = n;
        this.populationSize = populationSize;
        this.selectionPercent = selectionPercent;
        this.mutationPercent = mutationPercent;
    }


    @Override
    public void generateInitialPopulation() {
        population  = IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> new NQueensIndividual(randomBoard(n)))
                .collect(toList());
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
    public void calculateFitness() {

        population.parallelStream()
                .forEach(individual -> individual.setFitness(conflictScore(individual.getValue())));

        Collections.sort(population);
    }


    private double conflictScore(Integer[] board){
        // Initialize score to 0
        double score = 0.0;

        // We Iterate Over Every Queen Starting With The Far Left Queen
        for(int queen = 0; queen < board.length; queen++){

            // We Iterate Over Every Queen In Front Of The Queen Currently Being Evaluated
            for(int remainingQueen=queen+1; remainingQueen<board.length; remainingQueen++){

                // Check Row Conflict
                if(board[queen] == board[remainingQueen]){
                    score++;
                    continue;
                }

                // Check Diagonals Conflict
                if(Math.abs(board[queen]-board[remainingQueen]) == Math.abs(queen-remainingQueen)) {
                    score++;
                    continue;
                }

            }


        }

        return  score;

    }

    @Override
    public Individual getBestIndividual() {
        return Collections.min(population);
    }

    @Override
    public void selection() {
        int howManyToRemove = (int) Math.floor(selectionPercent * populationSize);
        for(int i=populationSize-1; i> (populationSize-howManyToRemove-1); i--)
            population.remove(i);
    }

    @Override
    public void crossover() {

        List<NQueensIndividual> newPopulation = IntStream.range(0, populationSize)
                .parallel()
                .mapToObj(i -> crossIndividuals())
                .collect(toList());

        population = newPopulation;

    }

    private NQueensIndividual crossIndividuals(){
        Random random = new Random();
        NQueensIndividual individual1 = population.get(random.nextInt(population.size()));
        NQueensIndividual individual2 = population.get(random.nextInt(population.size()));

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
    public void mutate() {
        population.parallelStream()
                .forEach(individual -> randomMutation(individual));

    }

    private void randomMutation(NQueensIndividual current){
        Random random = new Random();
        for(int i=0; i<n; i++)
            if(random.nextDouble() < mutationPercent)
                current.getValue()[i] = random.nextInt(n);
    }

    public List<NQueensIndividual> getPopulation(){
        return population;
    }


    private class NQueensIndividual extends Individual<Integer[]> {

        private Integer[] board;


        public NQueensIndividual(Integer[] board){
            this.board = board;
        }

        @Override
        public Integer[] getValue() {
            return board;
        }


        @Override
        public String toString(){

            StringBuilder sb = new StringBuilder();

            // Iterate Over Every Row And Column (Prints Row By Row)
            for(int row = 0; row < board.length; row++) {
                for (int col = 0; col < board.length; col++) {

                    // Print Q For Queen And * For Empty Space
                    if (board[col] == row)
                        sb.append("Q ");
                    else
                        sb.append("* ");
                }

                // Start New Row
                sb.append("\n");
            }

            return sb.toString();
        }
    }


    public static void main(String[] args){

        // Genetic Optimization Parameters //
        int maxGenerations = 2000;
        int populationSize = 10000;
        double selectionPercent = 0.8;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem problem = new ParaNQueenProblem(64, populationSize, selectionPercent, mutationPercent);

        GeneticOptimization optimizer = new GeneticOptimization(problem, maxGenerations);
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
