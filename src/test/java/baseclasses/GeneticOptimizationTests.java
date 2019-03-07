package baseclasses;

import com.knighten.ai.genetic.GeneticOptimization;
import com.knighten.ai.genetic.GeneticOptimizationParams;
import com.knighten.ai.genetic.Individual;
import com.knighten.ai.genetic.function.realvalue.MaximizeOneVar;
import com.knighten.ai.genetic.function.realvalue.MinimizeOneVar;
import com.knighten.ai.genetic.interfaces.IGenOptimizeProblem;
import com.knighten.ai.genetic.nqueens.NQueensIndividual;
import com.knighten.ai.genetic.nqueens.NQueensProblem;
import com.knighten.ai.genetic.stringmatch.RandomTextHelper;
import com.knighten.ai.genetic.stringmatch.StringIndividual;
import com.knighten.ai.genetic.stringmatch.StringMatchProblem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

public class GeneticOptimizationTests {

    private IGenOptimizeProblem mockProblem;
    private GeneticOptimizationParams mockParams;
    private NQueensIndividual mockIndividual;
    private List mockList;

    @Before
    public void setup() {
        mockIndividual = Mockito.mock(NQueensIndividual.class);
        when(mockIndividual.getFitness()).thenReturn(0.0);

        mockList = Mockito.mock(List.class);
        Mockito.when(mockList.get(0))
                .thenReturn(mockIndividual);

        mockProblem = Mockito.mock(IGenOptimizeProblem.class);
        Mockito.when(mockProblem.generateInitialPopulation(1))
                .thenReturn(mockList);
        Mockito.when(mockProblem.getBestIndividual(any()))
                .thenReturn(mockIndividual);


        mockParams = Mockito.mock(GeneticOptimizationParams.class);
        Mockito.when(mockParams.getPopulationSize())
                .thenReturn(1);
        Mockito.when(mockParams.getMaxGenerations())
                .thenReturn(1);
        Mockito.when(mockParams.getMutationProb())
                .thenReturn(.05);
        Mockito.when(mockParams.getSelectionPercent())
                .thenReturn(.25);
        Mockito.when(mockParams.getTargetValue())
                .thenReturn(Double.MAX_VALUE);

    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void constructorProblemIsNull() {
        new GeneticOptimization(null, mockParams);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorOptimizationParamsIsNull() {
        new GeneticOptimization(mockProblem, null);
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void optimizeCheckProblemMethodsAreCalled() {
        GeneticOptimization testObject = new GeneticOptimization(mockProblem, mockParams);
        testObject.optimize();

        verify(mockProblem, times(1)).generateInitialPopulation(1);
        verify(mockProblem, times(2)).calculateFitness(any());
        verify(mockProblem, times(2)).getBestIndividual(any());
        verify(mockProblem, times(1)).selection(any(), anyDouble());
        verify(mockProblem, times(1)).crossover(any(), anyInt());
        verify(mockProblem, times(1)).mutate(any(), anyDouble());

    }

    ///////////////////////////
    // Actual Algorithm Runs //
    ///////////////////////////

    @Test
    public void nQueensRun() {
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 5000, .05, .01);
        params.setTargetValue(0.0);

        IGenOptimizeProblem problem = new NQueensProblem(6, new Random(123));
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        List<Individual> optimizationGeneration = optimizer.optimize();

        Individual optimized = optimizationGeneration.get(optimizationGeneration.size() - 1);

        Assert.assertEquals(0.0, optimized.getFitness(), 0.000000001);
    }

    @Test
    public void realValueOneVarFunctionMinimizeRun() {
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 10000, .15, .01);
        params.setTargetValue(0.0);

        IGenOptimizeProblem problem = new MinimizeOneVar(-10.0,
                10.0,
                (x) -> Math.pow(x, 2),
                new Random(123));
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        List<Individual> optimizationGeneration = optimizer.optimize();

        Individual optimized = optimizationGeneration.get(optimizationGeneration.size() - 1);

        Assert.assertEquals(0.0, optimized.getFitness(), 0.01);
    }

    @Test
    public void realValueOneVarFunctionMaximizeRun() {
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 10000, .15, .01);
        params.setTargetValue(0.0);

        IGenOptimizeProblem problem = new MaximizeOneVar(-10.0,
                10.0,
                (x) -> Math.pow(x, 2),
                new Random(123));
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        List<Individual> optimizationGeneration = optimizer.optimize();

        Individual optimized = optimizationGeneration.get(optimizationGeneration.size() - 1);

        Assert.assertEquals(100.0, optimized.getFitness(), 0.01);
    }

    @Test
    public void stringMatchRun() {
        GeneticOptimizationParams params = new GeneticOptimizationParams(1000, 2000, .2, .01);
        params.setTargetValue(0.0);

        String validChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
        RandomTextHelper textHelper = new RandomTextHelper(new Random(), validChars);

        IGenOptimizeProblem<StringIndividual> problem = new StringMatchProblem("Hello String Matching",
                new Random(123), textHelper);
        GeneticOptimization optimizer = new GeneticOptimization(problem, params);

        List<Individual> optimizationGenerations = optimizer.optimize();

        Individual optimized = optimizationGenerations.get(optimizationGenerations.size() - 1);

        Assert.assertEquals(0.0, optimized.getFitness(), 0.000001);
    }

}
