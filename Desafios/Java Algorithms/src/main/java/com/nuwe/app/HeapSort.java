package com.nuwe.app;

public class HeapSort {

    // Método auxiliar para convertir el arreglo en un heap
    public static void heapify(int[] array, int n, int i) {
        int largest = i; // Inicializar el mayor como la raíz
        int left = 2 * i + 1; // Hijo izquierdo
        int right = 2 * i + 2; // Hijo derecho

        // Verificar si el hijo izquierdo es mayor que la raíz
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        // Verificar si el hijo derecho es mayor que la raíz
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // Si el mayor no es la raíz, intercambiamos
        if (largest != i) {
            int temp = array[i];
            array[i] = array[largest];
            array[largest] = temp;

            // Recursivamente hacer heapify en el subárbol afectado
            heapify(array, n, largest);
        }
    }

    // Método principal de ordenamiento HeapSort
    public static void heapSort(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int n = array.length;

        // Construir un heap máximo (max heap)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Extraer elementos del heap uno por uno
        for (int i = n - 1; i >= 0; i--) {
            // Mover el nodo actual a la raíz
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Llamar a heapify en el subárbol afectado
            heapify(array, i, 0);
        }
    }
}
