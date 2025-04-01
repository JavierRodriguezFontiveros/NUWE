import grpc
from concurrent import futures
from proto_generated import notification_pb2, notification_pb2_grpc
import redis
import json
from datetime import datetime

# Conexión a Redis - Usamos la base de datos 2
redis_client = redis.StrictRedis(host="redis", port=6379, db=2, decode_responses=True)

class NotificationService(notification_pb2_grpc.NotificationServiceServicer):

    def CreateNotification(self, request, context):
        """Crear una notificación para el receptor."""
        if not request.sender_email or not request.receiver_email:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Both sender_email and receiver_email are required.")
            return notification_pb2.CreateNotificationResponse(success=False)

        # Construimos la notificación en el formato correcto
        notification = {
            "sender_email": request.sender_email,
            "receiver_email": request.receiver_email,
            "timestamp": datetime.utcnow().isoformat(),
            "read": False
        }

        # Guardamos la notificación en la lista de notificaciones del receptor
        redis_client.rpush(f"notifications:{request.receiver_email}", json.dumps(notification))

        return notification_pb2.CreateNotificationResponse(success=True)

    def CheckUserSubscribed(self, request, context):
        """Comprobar si un usuario está suscrito a las notificaciones."""
        if not request.email:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Email is required.")
            return notification_pb2.CheckUserSubscribedResponse(subscribed=False)

        # Buscar el email en el hash global de suscripciones
        subscription_data = redis_client.hget("subscriptions", request.email)
        if subscription_data:
            subscription_info = json.loads(subscription_data)
            return notification_pb2.CheckUserSubscribedResponse(subscribed=subscription_info.get("subscribed", False))

        return notification_pb2.CheckUserSubscribedResponse(subscribed=False)

    def SubscribeUser(self, request, context):
        """Suscribir a un usuario a las notificaciones."""
        if not request.email:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Email is required.")
            return notification_pb2.SubscribeUserResponse(success=False)

        # Guardamos la suscripción en el hash global `subscriptions`
        redis_client.hset("subscriptions", request.email, json.dumps({"user_email": request.email, "subscribed": True}))

        return notification_pb2.SubscribeUserResponse(success=True)

    def UnsubscribeUser(self, request, context):
        """Cancelar la suscripción de un usuario a las notificaciones."""
        if not request.email:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Email is required.")
            return notification_pb2.UnsubscribeUserResponse(success=False)

        # Guardamos el estado de no suscrito en el hash global `subscriptions`
        redis_client.hset("subscriptions", request.email, json.dumps({"user_email": request.email, "subscribed": False}))

        return notification_pb2.UnsubscribeUserResponse(success=True)

# Servidor gRPC
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    notification_pb2_grpc.add_NotificationServiceServicer_to_server(NotificationService(), server)
    server.add_insecure_port("[::]:9898")
    print("Notification gRPC service running on port 9898...")
    server.start()
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
