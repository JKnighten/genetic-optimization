package nqueens;

import com.knighten.ai.genetic.nqueens.AbstractNQueensProblem;
import com.knighten.ai.genetic.nqueens.NQueensIndividual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AbstractNQueensProblemTests {

    private AbstractNQueensProblem testAbstractNQueensProblem;
    private NQueensIndividual mockIndividual1;
    private NQueensIndividual mockIndividual2;
    private NQueensIndividual mockIndividual3;

    @Before
    public void setup() {
        // Most Mock The Test Class Since It Is Abstract
        testAbstractNQueensProblem = Mockito.mock(AbstractNQueensProblem.class, Mockito.CALLS_REAL_METHODS);

        mockIndividual1 = Mockito.mock(NQueensIndividual.class);
        Mockito.when(mockIndividual1.getGenes())
                .thenReturn(new Integer[]{1, 3, 0, 2});

        mockIndividual2 = Mockito.mock(NQueensIndividual.class);
        Mockito.when(mockIndividual2.getGenes())
                .thenReturn(new Integer[]{0, 0, 0, 0});

        mockIndividual3 = Mockito.mock(NQueensIndividual.class);
        Mockito.when(mockIndividual3.getGenes())
                .thenReturn(new Integer[]{0, 1, 2, 3});
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void conflictScoreVerifyGenesUsed() {
        int score = testAbstractNQueensProblem.conflictScore(mockIndividual1);
        int score2 = testAbstractNQueensProblem.conflictScore(mockIndividual2);
        int score3 = testAbstractNQueensProblem.conflictScore(mockIndividual3);

        verify(mockIndividual1, times(1)).getGenes();
        Assert.assertEquals(0, score);

        verify(mockIndividual2, times(1)).getGenes();
        Assert.assertEquals(6, score2);

        verify(mockIndividual3, times(1)).getGenes();
        Assert.assertEquals(6, score3);
    }

}
