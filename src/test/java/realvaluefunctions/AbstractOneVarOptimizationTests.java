package realvaluefunctions;

import com.knighten.ai.genetic.function.realvalue.IOneVariableFunction;
import com.knighten.ai.genetic.function.realvalue.OneVarIndividual;
import com.knighten.ai.genetic.function.realvalue.AbstractOneVarOptimization;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class AbstractOneVarOptimizationTests {

    private AbstractOneVarOptimization testAbstractOneVarOptimization;
    private List<OneVarIndividual> listOfMockedIndividuals;
    private Random mockRandomForGenerateInitial;
    private Random mockRandomForMutate;
    private Random mockRandomForCrossover;
    private OneVarIndividual mockIndividualGenes1;
    private OneVarIndividual mockIndividualGenes2;
    private IOneVariableFunction mockFunction;
    private List<OneVarIndividual> mockListOfIndividuals;

    @Before
    public void setup() {
        // Most Mock The Test Class Since It Is Abstract
        testAbstractOneVarOptimization = Mockito.mock(AbstractOneVarOptimization.class, Mockito.CALLS_REAL_METHODS);

        mockRandomForGenerateInitial = Mockito.mock(Random.class);
        double[] randomDoubleStreamData = {1.0, 2.0};
        Mockito.when(mockRandomForGenerateInitial.doubles(2, -10, 10))
                .thenReturn(Arrays.stream(randomDoubleStreamData));

        mockRandomForCrossover = Mockito.mock(Random.class);
        int[] randInts = {0, 0, 1, 0};
        Mockito.when(mockRandomForCrossover.ints(2 * 2, 0, 2))
                .thenReturn(Arrays.stream(randInts));

        mockRandomForMutate = Mockito.mock(Random.class);
        double[] randXValues = {0.0, 3.0};
        double[] mutationChance = {.01, .95};
        Mockito.when(mockRandomForMutate.doubles(2, -10, 10))
                .thenReturn(Arrays.stream(randXValues));
        Mockito.when(mockRandomForMutate.doubles(2))
                .thenReturn(Arrays.stream(mutationChance));

        mockIndividualGenes1 = Mockito.mock(OneVarIndividual.class);
        Mockito.when(mockIndividualGenes1.getGenes())
                .thenReturn(1.0);
        mockIndividualGenes2 = Mockito.mock(OneVarIndividual.class);
        Mockito.when(mockIndividualGenes2.getGenes())
                .thenReturn(2.0);
        listOfMockedIndividuals = new ArrayList<>();
        listOfMockedIndividuals.add(mockIndividualGenes1);
        listOfMockedIndividuals.add(mockIndividualGenes2);

        // f(x) = x^2
        mockFunction = Mockito.mock(IOneVariableFunction.class);
        Mockito.when(mockFunction.getFuncValue(1.0))
                .thenReturn(1.0);
        Mockito.when(mockFunction.getFuncValue(2.0))
                .thenReturn(4.0);

        mockListOfIndividuals = Mockito.mock(ArrayList.class);
        Mockito.when(mockListOfIndividuals.size())
                .thenReturn(2);
        Mockito.when(mockListOfIndividuals.get(anyInt()))
                .thenReturn(mockIndividualGenes1);
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToInfinity() {
        testAbstractOneVarOptimization.setMaxDomain(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToNaN() {
        testAbstractOneVarOptimization.setMaxDomain(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToDoubleMaxValue() {
        testAbstractOneVarOptimization.setMaxDomain(Double.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToDoubleMinValue() {
        testAbstractOneVarOptimization.setMinDomain(Double.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToInfinity() {
        testAbstractOneVarOptimization.setMinDomain(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToNaN() {
        testAbstractOneVarOptimization.setMinDomain(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToDoubleMaxValue() {
        testAbstractOneVarOptimization.setMinDomain(Double.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToDoubleMinValue() {
        testAbstractOneVarOptimization.setMinDomain(Double.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFunctionToNull() {
        testAbstractOneVarOptimization.setFunction(null);
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void generateInitialPopulationCorrectObjectsCorrected() {
        testAbstractOneVarOptimization.setRandom(mockRandomForGenerateInitial);
        testAbstractOneVarOptimization.setMinDomain(-10.0);
        testAbstractOneVarOptimization.setMaxDomain(10.0);
        List<OneVarIndividual> results = testAbstractOneVarOptimization.generateInitialPopulation(2);

        // Correct Size Population Created
        Assert.assertEquals(2, results.size());

        // Ensure Individuals Created And Genes Are Set
        // Results Dependent On Mock mockRandomForGenerateInitial
        Assert.assertEquals(new Double(1.0), results.get(0).getGenes());
        Assert.assertEquals(new Double(2.0), results.get(1).getGenes());
    }

    @Test
    public void calculateFitnessEnsureGenesUsedToSetFitness() {
        testAbstractOneVarOptimization.setMinDomain(-10.0);
        testAbstractOneVarOptimization.setMaxDomain(10.0);
        testAbstractOneVarOptimization.setFunction(mockFunction);
        testAbstractOneVarOptimization.calculateFitness(listOfMockedIndividuals);

        // Ensure getGenes() Is Called Followed By setFitness()
        // Values In setFitness() Tests Are From mockFunction
        verify(mockIndividualGenes1, times(1)).getGenes();
        verify(mockIndividualGenes1, times(1)).setFitness(1.0);
        verify(mockIndividualGenes2, times(1)).getGenes();
        verify(mockIndividualGenes2, times(1)).setFitness(4.0);
    }

    @Test
    public void crossoverEnsureCorrectNumberOfIndividualsUsed() {
        testAbstractOneVarOptimization.setRandom(mockRandomForCrossover);
        testAbstractOneVarOptimization.crossover(mockListOfIndividuals, 2);

        // We Must Get 4 Individuals Total From Supplied Subpopulation
        verify(mockListOfIndividuals, times(4)).get(anyInt());
    }

    @Test
    public void crossoverEnsureCorrectGenesAreUsed() {
        testAbstractOneVarOptimization.setRandom(mockRandomForCrossover);
        testAbstractOneVarOptimization.crossover(listOfMockedIndividuals, 2);

        // Results Dependent On mockRandomForCrossover
        verify(mockIndividualGenes1, times(3)).getGenes();
        verify(mockIndividualGenes2, times(1)).getGenes();
    }

    @Test
    public void crossoverEnsureNewIndividualsGenesAreCorrect() {
        testAbstractOneVarOptimization.setRandom(mockRandomForCrossover);

        List<OneVarIndividual> listOfIndividuals = new ArrayList<>();
        listOfIndividuals.add(new OneVarIndividual(1.0));
        listOfIndividuals.add(new OneVarIndividual(2.0));
        List<OneVarIndividual> results = testAbstractOneVarOptimization.crossover(listOfIndividuals, 2);

        // Correct Population Size Generated
        Assert.assertEquals(2, results.size());

        // Correct Individuals Generated From Crossing
        // Results Dependent On mockRandomForCrossover
        Assert.assertEquals(new Double(1.5), results.get(0).getGenes());
        Assert.assertEquals(new Double(1.0), results.get(1).getGenes());
    }

    @Test
    public void mutateEnsureCorrectIndividualsSetGenesCalledWithRightParams() {
        testAbstractOneVarOptimization.setRandom(mockRandomForMutate);
        testAbstractOneVarOptimization.setMinDomain(-10.0);
        testAbstractOneVarOptimization.setMaxDomain(10.0);
        testAbstractOneVarOptimization.mutate(listOfMockedIndividuals, .05);

        // Results Dependent On mockRandomForMutate And listOfMockedIndividuals
        verify(mockIndividualGenes1, times(1)).setGenes(0.0);
    }

    @Test
    public void mutateEnsureCorrectNumberOfIndividualsSelectedFromPopulation() {
        testAbstractOneVarOptimization.setRandom(mockRandomForMutate);
        testAbstractOneVarOptimization.setMinDomain(-10.0);
        testAbstractOneVarOptimization.setMaxDomain(10.0);
        testAbstractOneVarOptimization.mutate(mockListOfIndividuals, .05);

        // Results Dependent On mockRandomForMutate
        verify(mockListOfIndividuals, times(1)).get(anyInt());
    }

}
