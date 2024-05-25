package com.jsrspring.screenmatch.main;

import java.util.Arrays;
import java.util.List;

public class ExampleStreams {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Joss", "Luis", "Brenda", "Maria Fernanda", "Eric", "Genesis");

        // streams => permite hacer muchas operaciones encadenadas
        names.stream()
                .sorted()
                .limit(4)
                .filter(n -> n.startsWith("J"))
                .map(String::toUpperCase) //.map(n -> n.toUpperCase())
                .forEach(System.out::println);
    }
}
