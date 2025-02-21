package com.nuwe.app;


import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private static int arr[];
    private static int length = 5000;
    private static int max =  (2) * length;
    private static int min = (-2) * length;
    private static JSONObject resultsObj = new JSONObject();
    private static FileWriter resultsFile;
    private static String filename = "./results.json";

    private static int currObjective = 0; 
    private static boolean[] objectives;


    @BeforeAll
    public static void beforeAllSetup(){
        //System.out.println("BeforeAllSetup()");
        objectives = new boolean[4];
        Arrays.fill(objectives, false);
    }

    @BeforeEach
    public void beforeEachSetup(){
        //System.out.println("beforeEachSetup()");
        arr = new int[length]; 
        Random rand = new Random();

        for (int i = 0; i < length; i ++){
            arr[i] = (int)Math.floor(Math.random() * (max - min + 1) + min);
        }
    }

    @AfterEach
    public void afterEachSetup(){
        //System.out.println("afterEachSetup()");
        int tmp = min;
        boolean isOrdered = true;
        for (int i : arr){
            isOrdered = (isOrdered && (tmp <= i));
            assertTrue(tmp <= i);
            tmp = i;
        }

        //System.out.println("Current objective : " +  currObjective + " with value " + isOrdered);
        objectives[currObjective - 1] = isOrdered;

    }

    @AfterAll
    public static void afterAllSetup(){
        // Fill resultsObject
        for (int i = 1; i <= 4; i++){
            resultsObj.put(i, objectives[i-1]);
        }
        try {
            resultsFile = new FileWriter(filename);
            resultsFile.write(resultsObj.toJSONString());
            resultsFile.flush();
            resultsFile.close();
        } catch (IOException e){
            System.out.println("[ERROR]: Error during writing to disk...");
            e.printStackTrace();
        }
    }

    @Test
    public void shouldBubbleSort(){
        currObjective  = 1;
        BubbleSort.bubbleSort(arr);
    }

    @Test
    public void shouldHeapSort(){
        currObjective  = 2;
        HeapSort.heapSort(arr);
    }

    @Test
    public void shouldMergeSort(){
        currObjective  = 3;
        MergeSort.mergeSort(arr);
    }

    @Test
    public void shouldQuickSort(){
        currObjective  = 4;
        QuickSort.quickSort(arr);
    }

}
