from project.objectives.priorityqueue import PriorityQueue
from project.common.node import Node

class HuffmanTree:
    def __init__(self):
        self.root = None  # Root node
        self.pq = PriorityQueue(compare=lambda x: x.freq)  # Ahora funciona correctamente



    def createHuffmanTree(self, data):
        # 1. Obtener las frecuencias
        if not isinstance(data, str) or not data:
            raise Exception("Input must be a non-empty string")
        freq_dict = self.frequencies(data)

        # 2. Crear nodos para cada caracter e insertarlos en la cola de prioridad
        for char, freq in freq_dict.items():
            self.pq.push(self.createNode(char, freq))

        # 3. Componer el árbol de Huffman
        while self.pq.length() > 1:
            # Obtener los dos nodos con menor frecuencia
            left = self.pq.pop()
            right = self.pq.pop()

            # Crear un nuevo nodo compuesto
            node = self.composeNode(left, right)

            # Insertar el nuevo nodo en la cola de prioridad
            self.pq.push(node)

        # El único nodo que queda es la raíz del árbol de Huffman
        self.root = self.pq.pop()

    def composeNode(self, left, right):
        if left is None or right is None:
            raise Exception("Both left and right nodes must be provided")
        return Node(None, left.freq + right.freq, left, right)

    def createNode(self, char, freq, left=None, right=None):
        return Node(char, freq, left, right)

    def frequencies(self, data):
        freq_dict = {}
        for char in data:
            if char in freq_dict:
                freq_dict[char] += 1
            else:
                freq_dict[char] = 1
        return freq_dict

    def getCodes(self):
        return self.getCodesRecursive(self.root, '')

    def getCodesRecursive(self, node, code):
        if node is None:
            return {}
        if node.char is not None:
            return {node.char: code}
        
        codes = {}
        codes.update(self.getCodesRecursive(node.left, code + '0'))
        codes.update(self.getCodesRecursive(node.right, code + '1'))
        return codes

    def printTree(self):
        self.printTreeRecursive(self.root, '')

    def printTreeRecursive(self, node, code):
        if node.char is not None:
            print("{} -> {}".format(node.char, code))

        if node.left is not None:
            self.printTreeRecursive(node.left, code + '0')
        if node.right is not None:
            self.printTreeRecursive(node.right, code + '1')


