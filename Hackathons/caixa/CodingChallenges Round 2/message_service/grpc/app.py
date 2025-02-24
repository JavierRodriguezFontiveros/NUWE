from proto_generated import message_pb2
from proto_generated import message_pb2_grpc
import grpc
from concurrent import futures
import redis
import json
from google.protobuf.timestamp_pb2 import Timestamp

# Conexión a Redis
r = redis.StrictRedis(host="redis", port=6379, db=1, decode_responses=True)

class MessageService(message_pb2_grpc.MessageServiceServicer):
    def SendMessage(self, request, context):
        sender_email = request.message.sender_email
        receiver_email = request.message.receiver_email
        content = request.message.content

        # Verificar que los usuarios existan
        if not r.exists(f"user:{sender_email}") or not r.exists(f"user:{receiver_email}"):
            context.set_details("Sender or Receiver not found")
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return message_pb2.SendMessageResponse(success=False, message="Sender or Receiver not found")

        # Crear un nuevo ID de mensaje y guardarlo en Redis
        message_id = r.incr("message_id_counter")
        message_key = f"message:{message_id}"

        timestamp = Timestamp()
        timestamp.GetCurrentTime()  # Establecer el timestamp actual

        message_data = {
            "message_id": message_id,
            "sender_email": sender_email,
            "receiver_email": receiver_email,
            "content": content,
            "timestamp": timestamp.ToJsonString()
        }
        r.set(message_key, json.dumps(message_data))

        # Asociar el mensaje con las conversaciones de los usuarios
        r.sadd(f"user:{sender_email}:conversations", message_id)
        r.sadd(f"user:{receiver_email}:conversations", message_id)

        return message_pb2.SendMessageResponse(success=True, message="Message sent successfully")

    def GetMessages(self, request, context):
        user_email = request.user_email

        try:
            # Obtener los IDs de los mensajes asociados al usuario
            message_ids = r.smembers(f"user:{user_email}:conversations")

            messages = []
            for message_id in message_ids:
                message_data = r.get(f"message:{message_id}")
                if message_data:
                    data = json.loads(message_data)

                    # Convertir timestamp a protobuf Timestamp
                    timestamp = Timestamp()
                    timestamp.FromJsonString(data["timestamp"])

                    # Construir el objeto Message
                    message = message_pb2.Message(
                        sender_email=data["sender_email"],
                        receiver_email=data["receiver_email"],
                        content=data["content"],
                        timestamp=timestamp
                    )
                    messages.append(message)

            return message_pb2.GetMessagesResponse(messages=messages)

        except redis.RedisError as e:
            context.set_details(f"Redis error: {str(e)}")
            context.set_code(grpc.StatusCode.UNAVAILABLE)
            return message_pb2.GetMessagesResponse(messages=[])

# Método para iniciar el servidor gRPC
def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    message_pb2_grpc.add_MessageServiceServicer_to_server(MessageService(), server)
    server.add_insecure_port("[::]:9696")
    server.start()
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
