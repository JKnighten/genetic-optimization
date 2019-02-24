package com.knighten.ai.genetic.function.realvalue;

import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class OptimizeOneVar implements IGenOptimizeProblem<OneVarIndividual> {

    private Random random;
    private double minDomain;
    private double maxDomain;
    private IOneVariableFunction function;

    void setRandom(Random random) {
        this.random = random;
    }

    void setMinDomain(double minDomain) {
        this.minDomain = minDomain;
    }

    void setMaxDomain(double maxDomain) {
        this.maxDomain = maxDomain;
    }

    void setFunction(IOneVariableFunction function) {
        this.function = function;
    }

    @Override
    abstract public OneVarIndividual getBestIndividual(List<OneVarIndividual> population);

    @Override
    abstract public List<OneVarIndividual> selection(List<OneVarIndividual> population,
                                                     double selectionPercent);

    @Override
    public List<OneVarIndividual> generateInitialPopulation(int populationSize) {
        return this.random.doubles(populationSize, minDomain, maxDomain)
                .mapToObj(OneVarIndividual::new)
                .collect(Collectors.toList());
    }

    @Override
    public void calculateFitness(List<OneVarIndividual> population) {
        population.stream().forEach(individual -> individual.setFitness(function.getFuncValue(individual.getGenes())));
        Collections.sort(population); // Sort Now To Make selection() More Efficient
    }

    @Override
    public List<OneVarIndividual> crossover(List<OneVarIndividual> subPopulation, int populationSize) {
        double[] genes= this.random.ints(populationSize*2,0, subPopulation.size())
                .mapToDouble(i -> subPopulation.get(i).getGenes())
                .toArray();

        return IntStream.range(0, populationSize)
                .mapToDouble(i -> (genes[i] + genes[i+populationSize])/2)
                .mapToObj(OneVarIndividual::new)
                .collect(Collectors.toList());
    }

    @Override
    public void mutate(List<OneVarIndividual> population, double mutationProb) {
        double[] randomMut = this.random.doubles(population.size()).toArray();
        double[] randomX = this.random.doubles(population.size(), minDomain, maxDomain).toArray();

        IntStream.range(0, population.size())
                .filter(i -> randomMut[i] < mutationProb)
                .forEach(i -> population.get(i).setGenes(randomX[i]));
    }
}
