package ru.javawebinar.basejava;

import java.util.*;
import java.util.stream.Collectors;


public class MainStream {

    static int minValue(int[] array) {
        return Arrays.stream(array)
                .distinct()
                .sorted()
                .reduce(0, (x1, x2) -> x1 * 10 + x2);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        final int sum = integers.stream().reduce(0, Integer::sum);
        return integers.stream()
                .filter(i -> sum % 2 != i % 2)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{5, 7, 9, 7, 5, 3, 5, 9, 3, 2, 1})); //123579
        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 1, 2, 3, 4, 5, 6, 7, 8, 9); // sum = 45, print 2 4 6 8
        oddOrEven(list).forEach(i -> System.out.print(i + " "));
    }
}
