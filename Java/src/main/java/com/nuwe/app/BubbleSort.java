package com.nuwe.app;

public class BubbleSort {

    // Método principal para ordenar el arreglo utilizando el algoritmo Bubble Sort
    static void bubbleSort(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int n = array.length;
        boolean swapped;

        // Bucle principal que recorre la lista
        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            // Comparar elementos adyacentes e intercambiarlos si están en el orden incorrecto
            for (int j = 0; j < n - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    // Intercambiar elementos
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;

                    swapped = true; // Se realizó un intercambio
                }
            }

            // Si no hubo intercambios en una pasada completa, la lista ya está ordenada
            if (!swapped) {
                break;
            }
        }
    }
}
