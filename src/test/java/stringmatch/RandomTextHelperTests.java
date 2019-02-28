package stringmatch;

import com.knighten.ai.genetic.stringmatch.RandomTextHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Random;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RandomTextHelperTests {

    private Random mockRandom;
    private String validCharacters;

    @Before
    public void setUp() {
        validCharacters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";

        mockRandom = Mockito.mock(Random.class);
        Mockito.when(mockRandom.ints(1, 0, validCharacters.length()))
                .thenReturn(Arrays.stream(new int[1]));
        Mockito.when(mockRandom.ints(5, 0, validCharacters.length()))
                .thenReturn(Arrays.stream(new int[5]));
        Mockito.when(mockRandom.ints(25, 0, validCharacters.length()))
                .thenReturn(Arrays.stream(new int[25]));
        Mockito.when(mockRandom.ints(1000, 0, validCharacters.length()))
                .thenReturn(Arrays.stream(new int[1000]));
        Mockito.when(mockRandom.ints(50000, 0, validCharacters.length()))
                .thenReturn(Arrays.stream(new int[50000]));
    }

    ////////////////////////
    // Parameter Checking //
    ////////////////////////

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullRandom() {
        new RandomTextHelper(null, validCharacters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullValidCharacters() {
        new RandomTextHelper(mockRandom, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorEmptyValidCharacters() {
        new RandomTextHelper(mockRandom, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateStringInvalidLength() {
        RandomTextHelper testObject = new RandomTextHelper(mockRandom, validCharacters);
        testObject.generateString(0);
    }

    ///////////////////
    // Check Methods //
    ///////////////////

    @Test
    public void generateCharVerifyRandomUsed() {
        RandomTextHelper testObject = new RandomTextHelper(mockRandom, validCharacters);
        testObject.generateChar();

        verify(mockRandom, times(1)).nextInt(validCharacters.length());
    }

    @Test
    public void generateStringVerifyRandomUsed() {
        RandomTextHelper testObject = new RandomTextHelper(mockRandom, validCharacters);
        testObject.generateString(5);

        verify(mockRandom, times(1)).ints(5, 0, validCharacters.length());
    }

    @Test
    public void generateStringVerifyStringLength() {
        RandomTextHelper testObject = new RandomTextHelper(mockRandom, validCharacters);

        Assert.assertEquals(1, testObject.generateString(1).length());
        Assert.assertEquals(5, testObject.generateString(5).length());
        Assert.assertEquals(25, testObject.generateString(25).length());
        Assert.assertEquals(1000, testObject.generateString(1000).length());
        Assert.assertEquals(50000, testObject.generateString(50000).length());
    }

}
