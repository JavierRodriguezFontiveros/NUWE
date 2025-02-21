package com.nuwe.app;

import java.util.Random;
import java.io.*;
import java.util.function.Consumer;

/**
 * Hello world!
 *
 */

public class App 
{
    public static void executeSort(Consumer<int[]> sortingFunction, int[] arr, String f) {

        long startTime = System.nanoTime();
 
        sortingFunction.accept(arr);

        // Stop measuring execution time
        long endTime = System.nanoTime();
        // Calculate the execution time in milliseconds
        long executionTime
            = (endTime - startTime) / 1000000;
 
        System.out.println("Sorting " + f + " takes "
                           + executionTime + "ms");
    }

    public static void main( String[] args )
    {
        int max = 1000;
        int min = -1000;
        int length = 133701;

        int arr[] = new int[length]; 
        Random rand = new Random();

        for (int i = 0; i < length; i ++){
            arr[i] = (int)Math.floor(Math.random() * (max - min + 1) + min);
        }


        int arr2[] = arr.clone();
        int arr3[] = arr.clone();
        int arr4[] = arr.clone();

        Consumer<int[]> sortBubble= BubbleSort::bubbleSort;
        Consumer<int[]> sortQuick = QuickSort::quickSort;
        Consumer<int[]> sortMerge = MergeSort::mergeSort;
        Consumer<int[]> sortHeap = HeapSort::heapSort;

        executeSort(sortQuick, arr2, "Quick Sort");
        executeSort(sortMerge, arr3, "Merge Sort");
        executeSort(sortHeap, arr4, "Heap Sort");
        executeSort(sortBubble, arr, "Bubble Sort");

        boolean isSorted = true;
        int tmp = min - 1;
        for (int i = 0; i < length; i++){
            if (arr[i] != arr2[i] || arr[i] != arr3[i] || arr[i] != arr4[i] ||
                arr2[i] != arr3[i] || arr2[i] != arr4[i] ||
                arr3[i] != arr4[i] )
                System.out.println("False at line " +  i);
            if (tmp > arr[i]){
                isSorted = false;
            }else {
                tmp = arr[i];
            }
            
        }

        System.out.println("Is the array correctly sorted? " + isSorted);

    }

    public static void printArray(int[] arr){
        for (int i = 0; i < arr.length; i++){
            System.out.print(arr[i] + " ");
        }
        System.out.println("\n");
    }
}
