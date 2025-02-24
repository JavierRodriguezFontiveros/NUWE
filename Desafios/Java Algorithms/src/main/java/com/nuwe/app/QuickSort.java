package com.nuwe.app;

public class QuickSort {

    // Método para ordenar el arreglo utilizando QuickSort
    public static void quickSort(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        quickSort(array, 0, array.length - 1);
    }

    // Método recursivo para ordenar el arreglo entre los índices low y high
    public static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // Encuentra el pivote tal que los elementos menores están a la izquierda y los mayores a la derecha
            int pi = partition(array, low, high);

            // Recursivamente ordenar las dos mitades
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    // Método para particionar el arreglo
    public static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        // Mover los elementos menores que el pivote a la izquierda
        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Colocar el pivote en su lugar correcto
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    // Método auxiliar para intercambiar dos elementos en el arreglo
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
