# NUWE Backend Challenge - Python - Algorithms 1

### Category   ➡️   Software

### Subcategory   ➡️   Python Backend

### Difficulty   ➡️   (Basic)

## 🌐 Background

As the world of backend development continues to expand, the ability to implement complex algorithms efficiently becomes increasingly valuable. NUWE's Backend Challenge in Python is designed to test these skills with a focus on algorithmic problem-solving. In this particular challenge, the spotlight is on the Huffman Coding algorithm, a cornerstone of modern text compression techniques.

Participants are tasked with developing a simple yet effective Huffman Coding algorithm to compress text. This algorithm intricately combines two essential data structures: a PriorityQueue and a tree. A PriorityQueue, as the name suggests, is a queue where elements are constantly ordered based on their priorities. Trees, on the other hand, are versatile structures widely used in various applications like file systems, natural language processing, and more.

## 🎯 Objectives

The objective of the challenge is to be able to implement a simple HuffmanCoding algorithm to compress text. This algorithm is based on a **PriorityQueue** and a **tree**. A **PriorityQueue** is Queue based on priorities, ie a constantly ordered queue.
A **tree** is a powerful data structure commonly used to represent (or manipulate) hierarchical data. This is used in File System structure, NLP, the ~~in~~famous DOM, and much more.
The **Huffman Coding** algorithm is the fundation of modern text compression. It stores the frequency of appearance for each different character in a text, and organizes them in a tree. 


1. **Objective 1**: Implement the PriorityQueue methods (`push()`,`pop()`,`isEmpty()`and `length()`). Modify the `priorityqueue.py` file.

2. **Objective 2**: Implement the functionality to create the HuffmanTree. These are the following functions: `frequencies()`, `createNode()` and `composeNode()`. Finally implement the functionaly to create a new HuffmanTree in the `createHuffmanTree()` function used all previous functions. Modify the `huffmantree.py` file.

3. **Objective 3**: Once the HuffmanTree is implemented next thing should be to get the **prefix code** for each symbol. Implement the `getCodes()` to traverse the HuffmanTree and return a dictionary with the codes for each symbol. It is strongly recommended to implement it recursively in the `getCodesRecursive()` function. Modify the `huffmantree.py` file.

4. **Objective 4**: Implement the HuffmanCoding `encode()` and `decode()` functions. Modify the `huffmancoding.py` file.

Each method is documented in its corresponding file. But it is strongly recommended to first understand how HuffmanCoding works to be able to solve this challenge! Good luck!

## ❓ Guides

### How to run

You can create your own testing functionality by adding logic and using the `main.py` present in the root directory.
Then you can run `make run` to run the main application.

### How to test

To test the the objectives run `make test` in the root folder.

### 📂 Repository Structure

A repository tree is provided below and should not be modified. Everything you need to develop the challenge is already included.
```bash
nuwe-backend-py-alg1/
├── main.py
├── Makefile
├── project
│   ├── common
│   │   ├── __init__.py
│   │   └── node.py
│   └── objectives
│       ├── huffmancoding.py
│       ├── huffmantree.py
│       ├── __init__.py
│       └── priorityqueue.py
├── README.md
├── requirements.txt
└── tests
    └── test_objectives.py
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
In this case, as can be seen in /tests, the tests to be performed are given, so no I/O example will be given. Based on these tests, you should be able to complete all objectives.

It is not allowed to modify the basic functionality of the tests provided. Complementary tests may be added, but these will not be evaluated.
