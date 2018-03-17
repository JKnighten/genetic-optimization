package com.knighten.ai.genetic.stringmatch;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StringMatchProblem implements IGenOptimizeProblem<StringMatchProblem.StringIndividual> {

    private ArrayList<StringIndividual> population;

    private String targetString;
    private int populationSize;

    public StringMatchProblem(String targetString, int populationSize){
        this.targetString = targetString;
        this.populationSize = populationSize;
    }


    @Override
    public List<StringMatchProblem.StringIndividual> generateInitialPopulation() {
        population = new ArrayList<>();

        List<StringMatchProblem.StringIndividual> initialPopulation = new ArrayList<>();

        for(int i=0; i<populationSize; i++) {
            population.add(new StringIndividual(RandomTextHelper.generateString(targetString.length())));
            initialPopulation.add(new StringIndividual(RandomTextHelper.generateString(targetString.length())));
        }

        return initialPopulation;
    }

    @Override
    public void calculateFitness(List<StringMatchProblem.StringIndividual> population) {

        for(StringIndividual individual: population) {

            double fitness = 0;
            for(int i=0; i<individual.getValue().length(); i++)
                fitness += Math.abs(individual.getValue().charAt(i) - targetString.charAt(i));
            individual.setFitness(fitness);

        }

        Collections.sort(population);
    }

    @Override
    public StringMatchProblem.StringIndividual getBestIndividual(List<StringIndividual> population) {
        return Collections.min(population);
    }

    @Override
    public List<StringMatchProblem.StringIndividual> selection(List<StringMatchProblem.StringIndividual> population, double selectionPercent) {
        int howManyToRemove = (int) Math.floor((1-selectionPercent) * populationSize);
        List<StringMatchProblem.StringIndividual> selectedPopulation = new ArrayList<>(population);
        for(int i=populationSize-1; i> (populationSize-howManyToRemove-1); i--)
            selectedPopulation.remove(i);

        return selectedPopulation;
    }

    @Override
    public List<StringMatchProblem.StringIndividual> crossover(List<StringMatchProblem.StringIndividual> selectedPopulation) {
        ArrayList<StringIndividual> newPopulation = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<populationSize; i++){
            StringIndividual individual1 = selectedPopulation.get(random.nextInt(selectedPopulation.size()));
            StringIndividual individual2 = selectedPopulation.get(random.nextInt(selectedPopulation.size()));

            int crossOverPoint = random.nextInt(targetString.length());
            String half1 = individual1.getValue().substring(0, crossOverPoint);
            String half2 = individual2.getValue().substring(crossOverPoint);
            StringIndividual crossedIndividual = new StringIndividual( half1 + half2);

            newPopulation.add(crossedIndividual);
        }

        return newPopulation;
    }

    @Override
    public void mutate(List<StringMatchProblem.StringIndividual> population, double mutationProb) {

        Random random = new Random();

        for(int j=0; j< populationSize; j++) {

            StringIndividual current = population.get(j);

            char[] charArray = current.getValue().toCharArray();


            for(int i=0; i<targetString.length(); i++)
                if(random.nextDouble() < mutationProb)
                    charArray[i] = RandomTextHelper.generateChar();

            String mutatedValue = new String(charArray);
            StringIndividual mutatedIndividual = new StringIndividual(mutatedValue);

            population.set(j, mutatedIndividual);
        }
    }




    public class StringIndividual extends Individual<String> {

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
        double selectionPercent = 0.2;
        double mutationPercent = 0.01;


        // Setup Problem //
        IGenOptimizeProblem<StringIndividual> problem = new StringMatchProblem("Chris mims is pew pew", populationSize);

        GeneticOptimization optimizer = new GeneticOptimization(problem, maxGenerations, selectionPercent, mutationPercent);
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



