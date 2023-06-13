
package com.ytree;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author nikolay.petkov
 */
public class WeightedProbabilityRandomNumberGeneratorTest {
    
    /**
     * The test demonstrates the usage by generating 100 random numbers and counting the occurrences of each number.
     */
    @Test
    public void randomGenerator_generatesNumbers_inAcceptableFrequency() {
        //given
        int[] randomNums = {-1, 0, 1, 2, 3};
        float[] probabilities = {0.01f, 0.3f, 0.58f, 0.1f, 0.01f};
        int testCount = 100;
        WeightedProbabilityRandomNumberGenerator randomGenerator =
                new WeightedProbabilityRandomNumberGenerator(randomNums, probabilities);
        Map<WeightedProbabilityNumber, Integer> testDataCapture =
                randomGenerator.probabilityNumbers.stream()
                        .collect(Collectors.toMap(Function.identity(), occurrences -> 0, (o1, o2) -> o1, TreeMap::new));
        //when
        //collect numbers from our nextNum() method
        for (int i = 0; i < testCount; i++) {
            int nextNum = randomGenerator.nextNum();
            //Increment the count for the selected number
            for (WeightedProbabilityNumber n : randomGenerator.probabilityNumbers) {
                if (n.value() == nextNum) {
                    int currentCount = testDataCapture.get(n);
                    testDataCapture.merge(n, currentCount, (oldCount, newCount) -> oldCount + 1);
                    break;
                }
            }
        }
        //then
        //check if nextNum() works correctly
        for (Map.Entry<WeightedProbabilityNumber, Integer> entry : testDataCapture.entrySet()) {
            WeightedProbabilityNumber wpn = entry.getKey();
            System.out.println(getTestSummary(wpn.value(), entry.getValue(), wpn.probability()));

            double achievedFrequency = entry.getValue() * Math.pow(testCount, -1);
            double frequencyGap = Math.abs(achievedFrequency - wpn.probability());
            assertTrue(frequencyGap < 0.1);
        }
    }

    private String getTestSummary(int number, int frequency, float weight) {
        return new StringBuilder()
                .append(number)
                .append(" occurrences -> ")
                .append(frequency)
                .append(" weight -> (")
                .append(weight)
                .append(")").toString();
    }
    
}
