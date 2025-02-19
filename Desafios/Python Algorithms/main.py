import sys
import re

from project.objectives.huffmantree import HuffmanTree
from project.objectives.huffmancoding import HuffmanCoding

data = "This is a test"

# Crear el árbol de Huffman
huffman = HuffmanTree()
huffman.createHuffmanTree(data)

# Obtener los códigos de Huffman
codes = huffman.getCodes()

# Codificar el texto
encodedText, huffmanTree = HuffmanCoding.encode(data)

# Decodificar el texto
decodedText = HuffmanCoding.decode(encodedText, huffmanTree)

# Imprimir los resultados
print("Data is {}".format(data))
print("Encoded data is {}".format(encodedText))
print("Decoded data is {}".format(decodedText))

# Calcular la longitud del código Huffman y la longitud original
lenHuff = len(encodedText)
lenData = len(data) * 8

# Diferencia de tamaño entre el texto original y el comprimido
diff = lenData - lenHuff

print("Bits in Huffman coding -> {}".format(lenHuff))
print("Bits without Huffman compression -> {}".format(lenData))
print("Difference {} - {} -> {}".format(lenData, lenHuff, diff))
