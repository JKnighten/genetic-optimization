package realvaluefunctions;

import com.knighten.ai.genetic.function.realvalue.OneVarIndividual;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class OneVarIndividualTests {

    @Before
    public void setup() {

    }

    @Test
    public void checkToStringIsValid() {
        OneVarIndividual testIndividual = new OneVarIndividual(1.0);
        Assert.assertEquals("1.0", testIndividual.toString());
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void createInvalidIndividualWithNaN() {
        OneVarIndividual testIndividual = new OneVarIndividual(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createInvalidIndividualWithInfinity() {
        OneVarIndividual testIndividual = new OneVarIndividual(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setGenesWithInvalidValueNaN() {
        OneVarIndividual testIndividual = new OneVarIndividual(0.0);
        testIndividual.setGenes(Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setGenesWithInvalidValueInfinity() {
        OneVarIndividual testIndividual = new OneVarIndividual(0.0);
        testIndividual.setGenes(Double.POSITIVE_INFINITY);
    }
}
