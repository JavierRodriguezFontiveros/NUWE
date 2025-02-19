import heapq

class PriorityQueue:
    def __init__(self, compare=lambda x: x):
        self.pq = []  # Lista vacía para almacenar la cola de prioridad
        self.compare = compare  # Función personalizada para comparar las prioridades

    def push(self, value):
        """
        Inserta un elemento en la PriorityQueue.
        @params value: El elemento que se va a insertar en la cola de prioridad.
        @returns: Ninguno
        """
        # Inserta el valor en el heap con su prioridad calculada por la función de comparación
        heapq.heappush(self.pq, (self.compare(value), value))

    def pop(self):
        """
        Devuelve y elimina el primer elemento de la cola.
        @returns: El primer elemento de la cola o None si está vacía.
        """
        if self.isEmpty():
            return None  # Si está vacía, devolvemos None
        return heapq.heappop(self.pq)[1]  # Devolvemos solo el elemento, no la prioridad

    def isEmpty(self):
        """
        Verifica si la PriorityQueue está vacía o no.
        @returns: True si la PriorityQueue está vacía, de lo contrario False.
        """
        return len(self.pq) == 0

    def length(self):
        """
        Devuelve la cantidad de elementos en la PriorityQueue.
        @returns: La cantidad de elementos en la PriorityQueue.
        """
        return len(self.pq)

    def __str__(self):
        # Para imprimir la cola en un formato legible por humanos
        return str([f for _, f in self.pq])
