import com.knighten.ai.genetic.function.realvalue.IOneVariableFunction;
import com.knighten.ai.genetic.function.realvalue.OneVarIndividual;
import com.knighten.ai.genetic.function.realvalue.OptimizeOneVar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class OptimizeOneVarTests {

    private OptimizeOneVar testOptimizeOneVar;
    private List<OneVarIndividual> listOfMockedIndividuals;
    private Random mockRandomForGenerateInitial;
    private Random mockRandomForMutate;
    private Random mockRandomForCrossover;
    private OneVarIndividual mockIndividualGenes1;
    private OneVarIndividual mockIndividualGenes2;
    private IOneVariableFunction mockFunction;

    @Before
    public void setup() {
        // Most Mock The Test Class Since It Is Abstract
        testOptimizeOneVar = Mockito.mock(OptimizeOneVar.class, Mockito.CALLS_REAL_METHODS);

        mockRandomForGenerateInitial = Mockito.mock(Random.class);
        double[] randomDoubleStreamData = {1.0, 2.0};
        Mockito.when(mockRandomForGenerateInitial.doubles(2, -10, 10))
                .thenReturn(Arrays.stream(randomDoubleStreamData));

        mockRandomForCrossover = Mockito.mock(Random.class);
        int[] randInts = {0 , 0, 1, 0};
        Mockito.when(mockRandomForCrossover.ints(2*2,0,2))
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

    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToInfinity() {
        testOptimizeOneVar.setMaxDomain(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToNaN() {
        testOptimizeOneVar.setMaxDomain(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToDoubleMaxValue() {
        testOptimizeOneVar.setMaxDomain(Double.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMaxDomainToDoubleMinValue() {
        testOptimizeOneVar.setMinDomain(Double.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToInfinity() {
        testOptimizeOneVar.setMinDomain(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToNaN() {
        testOptimizeOneVar.setMinDomain(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToDoubleMaxValue() {
        testOptimizeOneVar.setMinDomain(Double.MAX_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinDomainToDoubleMinValue() {
        testOptimizeOneVar.setMinDomain(Double.MIN_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFunctionToNull() {
        testOptimizeOneVar.setFunction(null);
    }

    ////////////////////
    // Method Outputs //
    ////////////////////

    @Test
    public void generateInitialPopulationCorrectObjectsCorrected() {
        testOptimizeOneVar.setRandom(mockRandomForGenerateInitial);
        testOptimizeOneVar.setMinDomain(-10.0);
        testOptimizeOneVar.setMaxDomain(10.0);
        List<OneVarIndividual> results = testOptimizeOneVar.generateInitialPopulation(2);


        Assert.assertEquals(2, results.size());
        Assert.assertEquals(new Double(1.0), results.get(0).getGenes());
        Assert.assertEquals(new Double(2.0), results.get(1).getGenes());
    }

    @Test
    public void calculateFitnessEnsureGenesUsedToSetFitness() {
        testOptimizeOneVar.setMinDomain(-10.0);
        testOptimizeOneVar.setMaxDomain(10.0);
        testOptimizeOneVar.setFunction(mockFunction);
        testOptimizeOneVar.calculateFitness(listOfMockedIndividuals);

        verify(mockIndividualGenes1, times(1)).getGenes();
        verify(mockIndividualGenes1, times(1)).setFitness(1.0);
        verify(mockIndividualGenes2, times(1)).getGenes();
        verify(mockIndividualGenes2, times(1)).setFitness(4.0);
    }

    @Test
    public void calculateFitnessEnsurePopulationIsSortedByFitness() {
        testOptimizeOneVar.setMinDomain(-10.0);
        testOptimizeOneVar.setMaxDomain(10.0);
        testOptimizeOneVar.setFunction(mockFunction);

        List<OneVarIndividual> listOfIndividuals = new ArrayList<>();
        listOfIndividuals.add(new OneVarIndividual(2.0));
        listOfIndividuals.add(new OneVarIndividual(1.0));

        testOptimizeOneVar.calculateFitness(listOfIndividuals);

        Assert.assertEquals(1.0, listOfIndividuals.get(0).getFitness(), 0.00000001);
        Assert.assertEquals(4.0, listOfIndividuals.get(1).getFitness(), 0.00000001);
    }


    @Test
    public void crossoverEnsureCorrectGenesAreUsed() {
        testOptimizeOneVar.setRandom(mockRandomForCrossover);
        testOptimizeOneVar.crossover(listOfMockedIndividuals, 2);

        verify(mockIndividualGenes1, times(3)).getGenes();
        verify(mockIndividualGenes2, times(1)).getGenes();
    }

    @Test
    public void crossoverEnsureNewIndividualsGenesAreCorrect() {
        testOptimizeOneVar.setRandom(mockRandomForCrossover);

        List<OneVarIndividual> listOfIndividuals = new ArrayList<>();
        listOfIndividuals.add(new OneVarIndividual(1.0));
        listOfIndividuals.add(new OneVarIndividual(2.0));

        List<OneVarIndividual> results = testOptimizeOneVar.crossover(listOfIndividuals, 2);

        Assert.assertEquals(2, results.size());
        Assert.assertEquals(new Double(1.5), results.get(0).getGenes());
        Assert.assertEquals(new Double(1.0), results.get(1).getGenes());
    }

    @Test
    public void mutateEnsureCorrectIndividualsSetGenesCalledWithRightParams() {
        testOptimizeOneVar.setRandom(mockRandomForMutate);
        testOptimizeOneVar.setMinDomain(-10.0);
        testOptimizeOneVar.setMaxDomain(10.0);

        testOptimizeOneVar.mutate(listOfMockedIndividuals, .05);

        verify(mockIndividualGenes1, times(1)).setGenes(0.0);
    }

}
