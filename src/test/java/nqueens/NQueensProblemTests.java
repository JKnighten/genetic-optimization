package nqueens;


import com.knighten.ai.genetic.nqueens.NQueensIndividual;
import com.knighten.ai.genetic.nqueens.NQueensProblem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class NQueensProblemTests {

    private Random mockRandom;
    private List<NQueensIndividual> listOfMockIndividuals;
    private NQueensIndividual mockIndividual0000;
    private NQueensIndividual mockIndividual1302;
    private List<NQueensIndividual> mockListOfIndividuals;

    @Before
    public void setUp() {
        mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.ints(4, 0, 4))
                .thenReturn(IntStream.range(0, 4))
                .thenReturn(IntStream.range(0, 4))
                .thenReturn(IntStream.range(0, 4))
                .thenReturn(IntStream.range(0, 4));
        Mockito.when(mockRandom.ints(2*2, 0, 2))
                .thenReturn(Arrays.stream(new int[]{0, 0, 1, 0}));
        Mockito.when(mockRandom.ints(2, 0, 4))
                .thenReturn(Arrays.stream(new int[]{1, 2}));
        double[] array = {.04, .95, .5, .5};
        List<Double> list = new ArrayList<>();
        Mockito.when(mockRandom.doubles(4))
                .thenReturn(Arrays.stream(array))
                .thenReturn(Arrays.stream(array));

        listOfMockIndividuals = new ArrayList<>();
        mockIndividual0000 = Mockito.mock(NQueensIndividual.class);
        Mockito.when(mockIndividual0000.getGenes())
                .thenReturn(new Integer[]{0, 0, 0, 0});
        mockIndividual1302 = Mockito.mock(NQueensIndividual.class);
        Mockito.when(mockIndividual1302.getGenes())
                .thenReturn(new Integer[]{1, 3, 0, 2});
        listOfMockIndividuals.add(mockIndividual1302);
        listOfMockIndividuals.add(mockIndividual0000);

        mockListOfIndividuals = Mockito.mock(ArrayList.class);
        Mockito.when(mockListOfIndividuals.size())
                .thenReturn(2);
        Mockito.when(mockListOfIndividuals.get(0))
                .thenReturn(mockIndividual1302);
        Mockito.when(mockListOfIndividuals.get(1))
                .thenReturn(mockIndividual0000);
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullRandom() {
        new NQueensProblem(4, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNLessThan4() {
        new NQueensProblem(3, mockRandom);
    }


    @Test
    public void generateInitialPopulationCorrectSizeReturned() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> result = testObject.generateInitialPopulation(4);

        Assert.assertEquals(4, result.size());
    }

    @Test
    public void calculateFitnessVerifyCorrectFitnessScoreAndCorrectUseOfIndividualsGenes() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        testObject.calculateFitness(listOfMockIndividuals);

        verify(mockIndividual0000, times(1)).getGenes();
        verify(mockIndividual0000, times(1)).setFitness(6);
        verify(mockIndividual1302, times(1)).getGenes();
        verify(mockIndividual1302, times(1)).setFitness(0);
    }

    @Test
    public void getBestIndividualCorrectIndividualRetrieved() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        NQueensIndividual results = testObject.getBestIndividual(listOfMockIndividuals);

        Assert.assertEquals(mockIndividual1302, results);
    }

    @Test
    public void selectionReturnsTheCorrectNumberOfIndividuals() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> results = testObject.selection(listOfMockIndividuals, .50);

        // 50% Of Array Size Returned
        Assert.assertEquals(results.size(), 1);

        // Result Contains The Individual With Smallest Fitness
        Assert.assertEquals(results.get(0), mockIndividual1302);
    }

    @Test
    public void selectionPopulationsGetCalledCorrectNumberOfTimes() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> results = testObject.selection(mockListOfIndividuals, .50);

        // Only One Element Should Be Returned, But May Need To Scan List To Find Which Elements To Remove
        verify(mockListOfIndividuals, atLeast(1)).get(anyInt());
    }

    @Test
    public void crossoverEnsureCorrectNumberOfIndividualsUsed() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> results = testObject.crossover(mockListOfIndividuals, 2);

        // We Must Get 4 Individuals Total From Supplied Subpopulation
        verify(mockListOfIndividuals, times(4)).get(anyInt());
    }

    @Test
    public void crossoverEnsureCorrectGenesAreUsed() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> results = testObject.crossover(listOfMockIndividuals, 2);

        // Results Dependent On mockRandom
        verify(mockIndividual1302, atLeast(3)).getGenes();
        verify(mockIndividual0000, atLeast(1)).getGenes();
    }

    @Test
    public void crossoverEnsureNewIndividualsGenesAreCorrect() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        List<NQueensIndividual> results = testObject.crossover(listOfMockIndividuals, 2);

        // Correct Population Size Generated
        Assert.assertEquals(2, results.size());

        // Correct Individuals Generated From Crossing
        // Results Dependent On mockRandom
        Assert.assertArrayEquals(new Integer[]{1, 0, 0, 0}, results.get(0).getGenes());
        Assert.assertArrayEquals(new Integer[]{1, 3, 0, 2}, results.get(1).getGenes());
    }

    @Test
    public void mutateEnsureCorrectIndividualsGenesAreUsed() {
        NQueensProblem testObject = new NQueensProblem(4, mockRandom);
        testObject.mutate(listOfMockIndividuals, .05);

        // Results Dependent On mockRandom And listOfMockIndividuals
        verify(mockIndividual1302, times(1)).getGenes();
        verify(mockIndividual1302, times(1)).setGenes(new Integer[]{0, 3, 0, 2});
        verify(mockIndividual0000, times(1)).getGenes();
        verify(mockIndividual0000, times(1)).setGenes(new Integer[]{0, 0, 0, 0});
    }

}
