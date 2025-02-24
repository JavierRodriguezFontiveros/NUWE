# NUWE Java Backend Challenge - Algorithms

### Category   ➡️   Software

### Subcategory   ➡️   Java Backend

### Difficulty   ➡️   (Basic)

## 🌐 Background

In an era where built-in methods and high-level functionalities often overshadow the basics, the NUWE Java Algorithms Challenge steps in to emphasize the importance of fundamental algorithmic skills. This challenge, classified as easy in difficulty, is a call for developers to reconnect with the core principles of computer science by implementing classic sorting algorithms in Java.

Participants will engage with four widely-known sorting algorithms: Bubble Sort, Heap Sort, Merge Sort, and Quick Sort. Each algorithm represents a unique approach to organizing data, and understanding these methods is key to developing a strong foundation in algorithmic thinking. The challenge is divided into four objectives, each focusing on a different sorting algorithm. Developers will be required to implement these algorithms in their respective Java files - BubbleSort.java, HeapSort.java, MergeSort.java, and QuickSort.java.

## 🎯 Objectives

In modern days, basic algorithms are usually not used as often as it should and programmers tend to rely on builtin `sort` methods. Understanding the fundamentals is crucial to build a great foundation.

This challenge proposes 4 common algorithms to sort arrays to be solved:

1. **Objective 1**: Implement the **BubbleSort** algorithm in the `BubbleSort.java` file.

2. **Objective 2**: Implement the **HeapSort** algorithm in the `HeapSort.java` file.

3. **Objective 3**: Implement the **MergeSort** algorithm in the `MergeSort.java` file.

4. **Objective 4**: Implement the **QuickSort** algorithm in the `QuickSort.java` file.

**Attention: There are function signatures already defined in each `.java` files. It is mandatory to implement the defined functions.** 

## ❓ Guides

**Bubble Sort** is considered the simplest sorting algorithm. It works by repeatedly swapping adjacent elements if they are in the wrong order. It has a time complexity of O(N^2)

**Heap Sort** is a comparison-based sorting technique based on Binary Heaps. It works by finding the minimum element and placing it at the beggining. Repeat the same process whilst pushing its value to the next minimum position.

**Merge Sort** is a sorting algorithm. It works by dividing an array into smaller subarrays, sorting each subarray, and then merging the sorted subarrays back together. It tends to be recursive.

**Quick Sort** is a sorting algorithm based on the Divide and Conquer algorith. It works by picking an element as a pivot, partion the given array around the pivot by placing the pivot in its correct position in the sorted array.

### 📂 Repository Structure

A repository tree is provided below and should not be modified. Everything you need to develop the challenge is already included.
```bash
nuwe-software-java-alg1/
├── mvnw
├── pom.xml
├── README.md
├── results.json
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── nuwe
│   │               └── app
│   │                   ├── App.java
│   │                   ├── BubbleSort.java
│   │                   ├── HeapSort.java
│   │                   ├── MergeSort.java
│   │                   └── QuickSort.java
│   └── test
│       └── java
│           └── com
│               └── nuwe
│                   └── app
│                       └── AppTest.java
└── target
    ├── classes
    │   └── com
    │       └── nuwe
    │           └── app
    │               ├── App.class
    │               ├── BubbleSort.class
    │               ├── HeapSort.class
    │               ├── MergeSort.class
    │               └── QuickSort.class
    └── test-classes
        └── com
            └── nuwe
                └── app
                    └── AppTest.class
```

**It is necessary to modify only the files proposed in the objectives.**

### 📊 Evaluation

The final score will be given according to whether or not the objectives have been met.

In this case, the challenge will be evaluated on 900 points which are distributed as follows:

- Objective 1: 225 points
- Objective 2: 225 points
- Objective 3: 225 points
- Objective 4: 225 points

### 📤 Submission

1. Solve the proposed objectives.
2. Push the changes you have made.
3. Click on Submit Challenge.
4. Wait for the results.

### ❓ Additional information

In this case, as can be seen in /src, the tests to be performed are given, so no I/O example will be given.
Based on these tests, you should be able to complete all objectives.

It is not allowed to modify the basic functionality of the tests provided. Complementary tests may be added, but these will not be evaluated.