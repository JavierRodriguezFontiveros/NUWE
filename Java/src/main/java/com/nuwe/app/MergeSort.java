package com.nuwe.app;

public class MergeSort {

    // Método para fusionar dos subarreglos
    public static void merge(int[] array, int left, int mid, int right) {
        int[] tempArray = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        // Fusionar las dos mitades
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                tempArray[k++] = array[i++];
            } else {
                tempArray[k++] = array[j++];
            }
        }

        // Copiar el resto de los elementos
        while (i <= mid) {
            tempArray[k++] = array[i++];
        }

        while (j <= right) {
            tempArray[k++] = array[j++];
        }

        // Copiar los elementos fusionados de vuelta al arreglo original
        System.arraycopy(tempArray, 0, array, left, tempArray.length);
    }

    // Método principal para ordenar el arreglo utilizando MergeSort
    public static void mergeSort(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }
        mergeSort(array, 0, array.length - 1);
    }

    // Método recursivo para dividir y ordenar el arreglo
    public static void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(array, left, mid);   // Ordenar la primera mitad
            mergeSort(array, mid + 1, right);  // Ordenar la segunda mitad

            merge(array, left, mid, right);  // Fusionar las dos mitades
        }
    }
}
