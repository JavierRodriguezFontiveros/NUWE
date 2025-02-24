import grpc
from concurrent import futures

# Importamos los archivos generados por protoc
from proto_generated import frontend_pb2
from proto_generated import frontend_pb2_grpc
from proto_generated import notification_pb2  # Importamos notification_pb2 correctamente

class FrontendService(frontend_pb2_grpc.FrontendServiceServicer):
    def ReceiveNotifications(self, request, context):
        """
        Método que recibe una lista de notificaciones desde NotificationService
        """
        # Iteramos sobre las notificaciones recibidas
        for notification in request.notifications:
            print(f"📩 Nueva notificación recibida de {notification.sender_email}")
            print(f"📆 Timestamp: {notification.timestamp} | 🔖 Estado de lectura: {notification.read}")
        
        # Aquí podrías almacenar las notificaciones o enviarlas a otro sistema.

        return frontend_pb2.NotificationResponse(success=True)

def serve():
    # Creamos el servidor gRPC
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    
    # Añadimos el servicio al servidor
    frontend_pb2_grpc.add_FrontendServiceServicer_to_server(FrontendService(), server)
    
    # Definimos el puerto donde escuchará el servicio
    server.add_insecure_port('[::]:3030')  # Puerto 3030 para gRPC
    print("🚀 Frontend gRPC server corriendo en puerto 3030...")

    # Iniciamos el servidor
    server.start()
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
