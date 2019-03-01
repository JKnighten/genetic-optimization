package nqueens;

import com.knighten.ai.genetic.nqueens.NQueensIndividual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NQueensIndividualTests {

    private Integer[] invalidLengthArray;
    private Integer[] invalidArrayValues;
    private Integer[] validArray;

    @Before
    public void setUp() {
        invalidLengthArray = new Integer[]{0, 1};
        invalidArrayValues = new Integer[]{9, 9, 9, 9, 9, 9};
        validArray = new Integer[]{0, 1, 2, 3,};
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorBoardIsNull() {
        NQueensIndividual testObject = new NQueensIndividual(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorBoardIsInvalidLength() {
        NQueensIndividual testObject = new NQueensIndividual(invalidLengthArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorBoardIsInvalidArrayValues() {
        NQueensIndividual testObject = new NQueensIndividual(invalidArrayValues);
    }

    @Test
    public void toStringCorrectStringGenerated() {
        NQueensIndividual testObject = new NQueensIndividual(validArray);
        Assert.assertEquals("Q * * * \n* Q * * \n* * Q * \n* * * Q ", testObject.toString());
    }
    
}
