package com.ytree;

import java.util.*;
import java.util.stream.IntStream;

public final class WeightedProbabilityRandomNumberGenerator {

    public final List<WeightedProbabilityNumber> probabilityNumbers;
    private final Random randomNumberGenerator = new Random();

    public WeightedProbabilityRandomNumberGenerator(int[] randomNums, float[] probabilities) {
        if (randomNums == null || probabilities == null || randomNums.length != probabilities.length) {
            throw new IllegalArgumentException();
        }
        // Add numbers to the list with their corresponding probability
        probabilityNumbers = IntStream.range(0, randomNums.length)
                .mapToObj(i -> new WeightedProbabilityNumber(randomNums[i], probabilities[i]))
                .toList();
    }

    /**
     * When this method is called multiple times over a long period,
     * it should return the numbers roughly with the initialized probabilities.
     * @return one of the randomNums.
     */
    public int nextNum() {
        List<Float> cumulativeProbabilities = new ArrayList<>();
        float sum = 0;
        //Calculate cumulative probabilities for each number
        for (WeightedProbabilityNumber n : probabilityNumbers) {
            sum += n.probability();
            cumulativeProbabilities.add(sum);
        }
        //Generate a random number between 0 and 1
        float nextFloat = randomNumberGenerator.nextFloat();
        //Select a number based on the cumulative probabilities
        //Find the first number whose cumulative probability is greater than the random number
        for (int i = 0; i < probabilityNumbers.size(); i++) {
            if (nextFloat < cumulativeProbabilities.get(i)) {
                return probabilityNumbers.get(i).value();
            }
        }
        //in case no number found return just last number.
//        return probabilityNumbers.get(probabilityNumbers.size() - 1).value();
        return Integer.MIN_VALUE;
    }
    
}

record WeightedProbabilityNumber(int value, float probability) implements Comparable<WeightedProbabilityNumber> {

    @Override
    public int compareTo(WeightedProbabilityNumber other) {
        return Integer.compare(this.value, other.value);
    }
}