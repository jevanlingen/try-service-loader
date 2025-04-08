package io.jacob;

import java.util.List;
import java.util.Map;

/**
 * This class represents the contests the players are gonna take.
 */
public interface Contest {
    @Experiment(value = 1, description = "Determine the highest value of the two numbers", shouldBeFixedBefore = "2025-04-08 12:30")
    int highestNumber(int a, int b);

    @Experiment(value = 2, description = "Remove duplicate characters from the input. Start from left to right.", shouldBeFixedBefore = "2025-04-08 12:45")
    String removeDuplicates(String s);

    @Experiment(value = 3, description = "Count the number of vowels (a, e, i, o, u) in the input string.", shouldBeFixedBefore = "2025-04-08 13:00")
    int countVowels(String input);

    // Enable more as we progress the hackathon

    /*@Experiment(value = 4, description = "Reverse the characters in the input string.", shouldBeFixedBefore = "2025-04-08 13:15")
    String reverseString(String input);

    @Experiment(value = 5, description = "Implement FizzBuzz. For numbers 1 to n, return 'Fizz' if divisible by 3, 'Buzz' if divisible by 5, 'FizzBuzz' if both, or the number itself.", shouldBeFixedBefore = "2025-04-08 13:30")
    List<String> fizzBuzz(int n);

    @Experiment(value = 6, description = "Determine if the input string is a palindrome (reads the same forward and backward).", shouldBeFixedBefore = "2025-04-08 13:45")
    boolean isPalindrome(String input);

    @Experiment(value = 7, description = "Return a map of word frequencies from the input string. Ignore casing and punctuation.", shouldBeFixedBefore = "2025-04-08 14:00")
    Map<String, Integer> wordFrequencies(String input);

    @Experiment(value = 8, description = "Sort the input list of words by their length (shortest to longest).", shouldBeFixedBefore = "2025-04-08 14:15")
    List<String> sortByLength(List<String> words);

    @Experiment(value = 9, description = "Group the given words by their first character.", shouldBeFixedBefore = "2025-04-08 14:30")
    Map<Character, List<String>> groupByFirstLetter(List<String> words);

    @Experiment(value = 10, description = "Find the longest common prefix among the given words.", shouldBeFixedBefore = "2025-04-08 14:45")
    String longestCommonPrefix(List<String> words);

    @Experiment(value = 11, description = "Return all anagrams of the given word from the list of candidates.", shouldBeFixedBefore = "2025-04-08 15:00")
    List<String> anagrams(String word, List<String> candidates);

    @Experiment(value = 12, description = "Flatten the nested list into a single list of elements.", shouldBeFixedBefore = "2025-04-08 15:15")
    <T> List<T> flatten(List<T> nestedList);

    @Experiment(value = 13, description = "Generate all prime numbers up to and including n.", shouldBeFixedBefore = "2025-04-08 15:30")
    List<Integer> primeNumbersUntil(int n);

    @Experiment(value = 14, description = "Compress the string using run-length encoding (e.g., 'aaabb' becomes 'a3b2').", shouldBeFixedBefore = "2025-04-08 15:45")
    String runLengthEncoding(String input);

    @Experiment(value = 15, description = "Evaluate a basic arithmetic expression given as a string. Assume no parentheses, but support +, -, *, /.", shouldBeFixedBefore = "2025-04-08 16:00")
    int evaluateExpression(String input);*/
}
