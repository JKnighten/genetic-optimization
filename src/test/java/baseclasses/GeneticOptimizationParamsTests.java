package baseclasses;

import com.knighten.ai.genetic.GeneticOptimizationParams;
import org.junit.Test;

public class GeneticOptimizationParamsTests {

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void constructorPopulationSizeLessThanOrEqualToZero() {
        new GeneticOptimizationParams(0, 10, .15, .05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorMaxGenerationsSizeLessThanOrEqualToZero() {
        new GeneticOptimizationParams(10, 0, .15, .05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorSelectionPercentageLessThanOrEqualToZero() {
        new GeneticOptimizationParams(10, 10, 0.0, .05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorSelectionPercentageGreaterThanOne() {
        new GeneticOptimizationParams(10, 10, 1.1, .05);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorMutationProbabilityLessThanZero() {
        new GeneticOptimizationParams(10, 10, .15, -0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorMutationProbabilityGreaterThanZero() {
        new GeneticOptimizationParams(10, 10, .15, 1.1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTargetValueTargetIsInfinity() {
        GeneticOptimizationParams testObject = new GeneticOptimizationParams(10, 10, .15, .10);
        testObject.setTargetValue(Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTargetValueTargetIsNaN() {
        GeneticOptimizationParams testObject = new GeneticOptimizationParams(10, 10, .15, .10);
        testObject.setTargetValue(Double.NaN);
    }

}
