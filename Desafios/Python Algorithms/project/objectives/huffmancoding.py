from project.objectives.huffmantree import HuffmanTree

class HuffmanCoding:
    """
    Encoding and decoding of data using Huffman Coding text compression
    """

    @staticmethod
    def encode(data):
        """
        Generates a HuffmanTree and creates an encoded data (in binary string) from the data parameter.
        @params: data is a string of characters.
        @returns: A tuple with the encoded data and the associated HuffmanTree.
        """
        # Crear el árbol de Huffman
        huffman_tree = HuffmanTree()
        huffman_tree.createHuffmanTree(data)

        # Obtener los códigos generados para cada símbolo
        codes = huffman_tree.getCodes()

        # Codificar el texto en binario
        encoded_data = ''.join(codes[char] for char in data)

        # Retornar el binario comprimido y el árbol de Huffman
        return encoded_data, huffman_tree

    @staticmethod
    def decode(encoded_data, huffman_tree):
        """
        Decodes into text the encodedData using the huffmantree passed as a parameter.
        @param encoded_data: Binary string representation of encoded text. 
        @param huffman_tree: HuffmanTree object representation created from the decoded version of the encoded data. 
        This will be used to decode the encodedData parameter.
        @returns: decoded data (plain text) using the huffman_tree and the encoded_data parameters.
        """
        current_node = huffman_tree.root
        decoded_text = []

        # Recorrer el binario y reconstruir el texto original
        for bit in encoded_data:
            # Si el bit es 0, ir al hijo izquierdo
            if bit == '0':
                current_node = current_node.left
            # Si el bit es 1, ir al hijo derecho
            else:
                current_node = current_node.right

            # Si es una hoja, agregar el carácter al texto decodificado
            if current_node.char is not None:
                decoded_text.append(current_node.char)
                current_node = huffman_tree.root  # Volver a la raíz para continuar con el siguiente símbolo

        # Unir los caracteres y retornar el texto original
        return ''.join(decoded_text)
