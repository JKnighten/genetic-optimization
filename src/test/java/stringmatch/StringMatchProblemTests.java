package stringmatch;

import com.knighten.ai.genetic.stringmatch.RandomTextHelper;
import com.knighten.ai.genetic.stringmatch.StringIndividual;
import com.knighten.ai.genetic.stringmatch.StringMatchProblem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class StringMatchProblemTests {

    private Random mockRandom;
    private RandomTextHelper mockTextHelper;
    private List<StringIndividual> listOfMockIndividuals = new ArrayList<>();
    private StringIndividual mockIndividualGenesSDRS;
    private StringIndividual mockIndividualGenesTESS;
    private List<StringIndividual> mockListOfIndividuals;

    @Before
    public void setUp() {
        mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.ints(2*2, 0, 2))
                .thenReturn(Arrays.stream(new int[]{0, 0, 1, 0}));
        Mockito.when(mockRandom.ints(2, 0, 4))
                .thenReturn(Arrays.stream(new int[]{1, 2}));
        double[] array = {.04, .95, .5, .5};
        List<Double> list = new ArrayList<>();
        Mockito.when(mockRandom.doubles(4))
                .thenReturn(Arrays.stream(array))
                .thenReturn(Arrays.stream(array));

        mockTextHelper = Mockito.mock(RandomTextHelper.class);
        Mockito.when(mockTextHelper.generateString(4))
                .thenReturn("test");
        Mockito.when(mockTextHelper.generateChar())
                .thenReturn('a');

        mockIndividualGenesTESS = Mockito.mock(StringIndividual.class);
        Mockito.when(mockIndividualGenesTESS.getGenes())
                .thenReturn("tess");
        Mockito.when(mockIndividualGenesTESS.getFitness())
                .thenReturn(1.0);
        mockIndividualGenesSDRS = Mockito.mock(StringIndividual.class);
        Mockito.when(mockIndividualGenesSDRS.getGenes())
                .thenReturn("sdrs");
        Mockito.when(mockIndividualGenesSDRS.getFitness())
                .thenReturn(4.0);
        listOfMockIndividuals.add(mockIndividualGenesTESS);
        listOfMockIndividuals.add(mockIndividualGenesSDRS);

        mockListOfIndividuals = Mockito.mock(ArrayList.class);
        Mockito.when(mockListOfIndividuals.size())
                .thenReturn(2);
        Mockito.when(mockListOfIndividuals.get(anyInt()))
                .thenReturn(mockIndividualGenesTESS);
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullTargetString() {
        new StringMatchProblem(null, mockRandom, mockTextHelper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorEmptyTargetString() {
        new StringMatchProblem("", mockRandom, mockTextHelper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullRandom() {
        new StringMatchProblem("test", null, mockTextHelper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullRandomTextHelper() {
        new StringMatchProblem("test", mockRandom, null);
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void generateInitialPopulationCorrectMethodsUsed() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        testObject.generateInitialPopulation(10);

        verify(mockTextHelper, times(10)).generateString(4);
    }

    @Test
    public void calculateFitnessVerifyCorrectFitnessScoreAndCorrectUseOfIndividualsGenes() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        testObject.calculateFitness(listOfMockIndividuals);

        verify(mockIndividualGenesSDRS, atLeast(1)).getGenes();
        verify(mockIndividualGenesSDRS, times(1)).setFitness(4);
        verify(mockIndividualGenesTESS, atLeast(1)).getGenes();
        verify(mockIndividualGenesTESS, times(1)).setFitness(1);
    }

    @Test
    public void getBestIndividualCorrectIndividualRetrieved() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        StringIndividual results = testObject.getBestIndividual(listOfMockIndividuals);

        Assert.assertEquals(mockIndividualGenesTESS, results);
    }

    @Test
    public void selectionReturnsTheCorrectNumberOfIndividuals() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        List<StringIndividual> results = testObject.selection(listOfMockIndividuals, .50);

        // 50% Of Array Size Returned
        Assert.assertEquals(results.size(), 1);

        // Result Contains The Individual With Smallest Fitness
        Assert.assertEquals(results.get(0), mockIndividualGenesTESS);
    }

    @Test
    public void selectionPopulationsGetCalledCorrectNumberOfTimes() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        List<StringIndividual> results = testObject.selection(mockListOfIndividuals, .50);

        // Only One Element Should Be Returned, But May Need To Scan List To Find Which Elements To Remove
        verify(mockListOfIndividuals, atLeast(1)).get(anyInt());
    }

    @Test
    public void crossoverEnsureCorrectNumberOfIndividualsUsed() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        List<StringIndividual> results = testObject.crossover(mockListOfIndividuals, 2);

        // We Must Get 4 Individuals Total From Supplied Subpopulation
        verify(mockListOfIndividuals, times(4)).get(anyInt());
    }

    @Test
    public void crossoverEnsureCorrectGenesAreUsed() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        List<StringIndividual> results = testObject.crossover(listOfMockIndividuals, 2);

        // Results Dependent On mockRandom
        verify(mockIndividualGenesTESS, atLeast(3)).getGenes();
        verify(mockIndividualGenesSDRS, atLeast(1)).getGenes();
    }

    @Test
    public void crossoverEnsureNewIndividualsGenesAreCorrect() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        List<StringIndividual> results = testObject.crossover(listOfMockIndividuals, 2);

        // Correct Population Size Generated
        Assert.assertEquals(2, results.size());

        // Correct Individuals Generated From Crossing
        // Results Dependent On mockRandom
        Assert.assertEquals("tdrs", results.get(0).getGenes());
        Assert.assertEquals("tess", results.get(1).getGenes());
    }

    @Test
    public void mutateEnsureCorrectIndividualsSetGenesCalledWithRightParams() {
        StringMatchProblem testObject = new StringMatchProblem("test", mockRandom, mockTextHelper);
        testObject.mutate(listOfMockIndividuals, .05);

        // Results Dependent On mockRandom And listOfMockIndividuals
        verify(mockIndividualGenesTESS, times(1)).getGenes();
        verify(mockIndividualGenesTESS, times(1)).setGenes("aess");
        verify(mockIndividualGenesSDRS, times(1)).getGenes();
        verify(mockIndividualGenesSDRS, times(1)).setGenes("adrs");
    }

}
