package realvaluefunctions;

import com.knighten.ai.genetic.function.realvalue.IOneVariableFunction;
import com.knighten.ai.genetic.function.realvalue.MinimizeOneVar;
import com.knighten.ai.genetic.function.realvalue.OneVarIndividual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class MinimizeOneVarTests {

    private IOneVariableFunction mockFunction;
    private Random mockRandom;
    private OneVarIndividual mockIndividualGenes1;
    private OneVarIndividual mockIndividualGenes2;
    private List<OneVarIndividual> listOfMockedIndividuals;
    private List<OneVarIndividual> mockListOfIndividuals;

    @Before
    public void setup() {

        mockFunction = Mockito.mock(IOneVariableFunction.class);
        mockRandom = Mockito.mock(Random.class);

        // Fitness Assuming Function Is (x) -> Math.pow(x, 2)
        mockIndividualGenes1 = Mockito.mock(OneVarIndividual.class);
        Mockito.when(mockIndividualGenes1.getFitness())
                .thenReturn(1.0);
        mockIndividualGenes2 = Mockito.mock(OneVarIndividual.class);
        Mockito.when(mockIndividualGenes2.getFitness())
                .thenReturn(4.0);
        listOfMockedIndividuals = new ArrayList<>();
        listOfMockedIndividuals.add(mockIndividualGenes1);
        listOfMockedIndividuals.add(mockIndividualGenes2);

        mockListOfIndividuals = Mockito.mock(ArrayList.class);
        Mockito.when(mockListOfIndividuals.size())
                .thenReturn(2);
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void getBestIndividualEnsureIndividualWithLowestFitnessReturned() {
        MinimizeOneVar testObject = new MinimizeOneVar(-10,10, mockFunction, mockRandom);
        OneVarIndividual result = testObject.getBestIndividual(listOfMockedIndividuals);

        Assert.assertEquals(result, mockIndividualGenes1);
    }

    @Test
    public void getBestIndividualEnsureGetOnSubPopCalledAtLeastOnce() {
        MinimizeOneVar testObject = new MinimizeOneVar(-10,10, mockFunction, mockRandom);
        testObject.getBestIndividual(mockListOfIndividuals);

        // At The Very Least We Need To Get The Best Element Out Of The Population
        verify(mockListOfIndividuals, atLeast(1)).get(anyInt());
    }


    @Test
    public void selectionReturnsTheCorrectNumberOfIndividuals() {
        MinimizeOneVar testObject = new MinimizeOneVar(-10,10, mockFunction, mockRandom);
        List<OneVarIndividual> result = testObject.selection(listOfMockedIndividuals, .50);

        // 50% Of Array Size Returned
        Assert.assertEquals(result.size(), 1);

        // Result Contains The Individual With Smallest Fitness
        Assert.assertEquals(result.get(0), mockIndividualGenes1);
    }

    @Test
    public void selectionPopulationsGetCalledCorrectNumberOfTimes() {
        MinimizeOneVar testObject = new MinimizeOneVar(-10,10, mockFunction, mockRandom);
        List<OneVarIndividual> result = testObject.selection(mockListOfIndividuals, .50);

        // Only One Element Should Be Returned, But May Need To Scan List To Find Which Elements To Remove
        verify(mockListOfIndividuals, atLeast(1)).get(anyInt());
    }

}
