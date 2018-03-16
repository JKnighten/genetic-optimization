package com.knighten.ai.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NQueenProblem implements IGenOptimizeProblem {

    private ArrayList<NQueensIndividual> population;

    private int n;
    private int populationSize;
    private  double selectionPercent;
    private  double mutationPercent;

    public NQueenProblem(int n, int populationSize, double selectionPercent, double mutationPercent){
        this.n = n;
        this.populationSize = populationSize;
        this.selectionPercent = selectionPercent;
        this.mutationPercent = mutationPercent;
    }


    @Override
    public void generateInitialPopulation() {
        population = new ArrayList<>();

        Random random = new Random();

        for(int i=0; i<populationSize; i++){
            Integer[] board = new Integer[n];
            for(int j=0; j<n; j++){
                board[j] = random.nextInt(n);
            }

            population.add(new NQueensIndividual(board));

        }
    }

    @Override
    public void calculateFitness() {
        for(NQueensIndividual individual: population)
            individual.setFitness(conflictScore(individual.getValue()));

        Collections.sort(population);
    }


    public double conflictScore(Integer[] board){
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

        ArrayList<NQueensIndividual> newPopulation = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<populationSize; i++){
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


            NQueensIndividual crossedIndividual = new NQueensIndividual(crossedIndividualBoard);

            newPopulation.add(crossedIndividual);
        }

        population = newPopulation;

    }

    @Override
    public void mutate() {

        Random random = new Random();

        for(int j=0; j< populationSize; j++) {

            NQueensIndividual current = population.get(j);

            for(int i=0; i<n; i++)
                if(random.nextDouble() < mutationPercent)
                    current.getValue()[i] = random.nextInt(n);

        }

    }

    public ArrayList<NQueensIndividual> getPopulation(){
        return population;
    }


    private class NQueensIndividual extends Individual<Integer[]> {

        public NQueensIndividual(Integer[] board){
            this.setValue(board);
        }

        @Override
        public String toString(){

            StringBuilder sb = new StringBuilder();
            Integer[] board = this.getValue();

            // Iterate Over Every Row And Column (Prints Row By Row)
            for(int row = 0; row < board.length; row++) {
                for (int col = 0; col < board.length; col++) {

                    // Print Q For Queen And * For Empty Space
                    String spaceValue = board[col] == row ? "Q " : "* ";
                    sb.append(spaceValue);
                }

                // Start New Row
                sb.append("\n");
            }

            return sb.toString();
        }
    }

    public static void main(String[] args){

        // Genetic Optimization Parameters //
        int maxGenerations = 5000;
        int populationSize = 100;
        double selectionPercent = 0.8;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem problem = new NQueenProblem(32, populationSize, selectionPercent, mutationPercent);

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
