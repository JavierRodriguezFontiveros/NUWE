class Node: 
    def __init__(self, char, freq, left=None, right=None):
        self.char = char
        self.freq = freq
        self.left = left
        self.right = right

    def __lt__(self, other):
        # Permite que los nodos se comparen por su frecuencia
        return self.freq < other.freq

    def __str__(self):
        return "Char: {} | Freq: {}".format(self.char, self.freq)

    def __repr__(self):
        return f"Node({self.char!r}, {self.freq})"
