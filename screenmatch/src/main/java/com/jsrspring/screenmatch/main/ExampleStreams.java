package com.jsrspring.screenmatch.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ExampleStreams {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Joss", "Luis", "Brenda", "Maria Fernanda", "Eric", "Genesis");

        // streams => permite hacer muchas operaciones encadenadas
        names.stream()
                .sorted()
                .limit(5)
                .filter(n -> n.startsWith("J"))
                .map(String::toUpperCase) //.map(n -> n.toUpperCase())
                .forEach(System.out::println);

        // filtrar una lista de números para devolver solo los números pares.
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        List<Integer> pairsNumbers = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println(pairsNumbers); // Salida: [2, 4, 6, 8, 10]

        // Transformar una lista de cadenas en una lista de sus respectivas longitudes
        List<String> words = Arrays.asList("Java", "Stream", "Operaciones", "Intermedias");

        List<Integer> sizeString = words.stream()
                .map(String::length)
                .collect(Collectors.toList());

        System.out.println(sizeString); // Salida: [4, 6, 11, 17]

        /*
        * Collect: permite recopilar los elementos de la stream en una colección o en otro tipo de dato.
        * Por ejemplo, podemos recopilar los números pares en un conjunto.
        * */
        List<Integer> newNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        Set<Integer> newPairNumbers = newNumbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toSet());

        System.out.println(newPairNumbers); // Salida: [2, 4, 6, 8, 10]

    }
}
