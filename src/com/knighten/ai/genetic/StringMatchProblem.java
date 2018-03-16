package com.knighten.ai.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StringMatchProblem implements IGenOptimizeProblem {

    private ArrayList<StringIndividual> population;

    private String targetString;
    private int populationSize;
    private  double selectionPercent;
    private  double mutationPercent;

    public StringMatchProblem(String targetString, int populationSize, double selectionPercent, double mutationPercent){
        this.targetString = targetString;
        this.populationSize = populationSize;
        this.selectionPercent = selectionPercent;
        this.mutationPercent = mutationPercent;
    }


    @Override
    public void generateInitialPopulation() {
        population = new ArrayList<>();

        for(int i=0; i<populationSize; i++)
            population.add(new StringIndividual(RandomTextHelper.generateString(targetString.length())));
    }

    @Override
    public void calculateFitness() {

        for(StringIndividual individual: population) {

            double fitness = 0;
            for(int i=0; i<individual.getValue().length(); i++)
                fitness += Math.abs(individual.getValue().charAt(i) - targetString.charAt(i));
            individual.setFitness(fitness);

        }

        Collections.sort(population);
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
        ArrayList<StringIndividual> newPopulation = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<populationSize; i++){
            StringIndividual individual1 = population.get(random.nextInt(population.size()));
            StringIndividual individual2 = population.get(random.nextInt(population.size()));

            int crossOverPoint = random.nextInt(targetString.length());
            String half1 = individual1.getValue().substring(0, crossOverPoint);
            String half2 = individual2.getValue().substring(crossOverPoint);
            StringIndividual crossedIndividual = new StringIndividual( half1 + half2);

            newPopulation.add(crossedIndividual);
        }

        population = newPopulation;
    }

    @Override
    public void mutate() {

        Random random = new Random();

        for(int j=0; j< populationSize; j++) {

            StringIndividual current = population.get(j);

            char[] charArray = current.getValue().toCharArray();


            for(int i=0; i<targetString.length(); i++)
                if(random.nextDouble() < mutationPercent)
                    charArray[i] = RandomTextHelper.generateChar();

            String mutatedValue = new String(charArray);
            StringIndividual mutatedIndividual = new StringIndividual(mutatedValue);

            population.set(j, mutatedIndividual);
        }
    }




    private class StringIndividual extends Individual<String> {

        public StringIndividual(String stringValue){
            this.setValue(stringValue);
        }

        @Override
        public String toString(){ return this.getValue(); }

    }

    public static void main(String[] args){

        // Genetic Optimization Parameters //
        int maxGenerations = 2000;
        int populationSize = 1000;
        double selectionPercent = 0.8;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem problem = new StringMatchProblem("Chris mims is pew pew", populationSize,
                selectionPercent, mutationPercent);

        GeneticOptimization optimizer = new GeneticOptimization(problem, maxGenerations);
        long startTime = System.nanoTime();
        List<Individual> optimizationGeneration = optimizer.optimize();
        long endTime = System.nanoTime();

        for(int i=1; i<optimizationGeneration.size(); i++) {
            if(optimizationGeneration.get(i).getFitness() < optimizationGeneration.get(i-1).getFitness() ){
                String individualString = optimizationGeneration.get(i).toString();
                double fitness = optimizationGeneration.get(i).getFitness();
                System.out.println("Generation " + i + ": \n" + individualString + " Score - " + fitness);
            }
        }

        double duration = (endTime - startTime)/1000000.0;

        System.out.println("Optimization Duration: " + duration + " ms");
        System.out.println("Generations: " + optimizationGeneration.size());
    }



}



