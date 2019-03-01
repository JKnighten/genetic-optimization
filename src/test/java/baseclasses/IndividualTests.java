package baseclasses;

import com.knighten.ai.genetic.Individual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndividualTests {

    private Individual individualFitness1;
    private Individual individualFitness2;

    @Before
    public void setup() {
        // Most Mock The Test Class Since It Is Abstract
        individualFitness1 = new Individual();
        individualFitness2 = new Individual();

        individualFitness1.setFitness(1.0);
        individualFitness2.setFitness(2.0);
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void setFitnessToNaNOrInfinity() {
        individualFitness1.setFitness(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setGenesToNull() {
        individualFitness1.setGenes(null);
    }

    ////////////////////
    // Method Testing //
    ////////////////////

    @Test
    public void compareToEqualIndividuals() {
        Assert.assertEquals(0, individualFitness1.compareTo(individualFitness1));
    }

    @Test
    public void compareToGreaterThan() {
        Assert.assertEquals(1, individualFitness2.compareTo(individualFitness1));
    }

    @Test
    public void compareToLessThan() {
        Assert.assertEquals(-1, individualFitness1.compareTo(individualFitness2));
    }

}

