from flask import Flask, request, jsonify
import redis
import re

app = Flask(__name__)
r = redis.StrictRedis(host="redis", port=6379, db=1, decode_responses=True)

def validate_email(email):
    # Utiliza una expresión regular simple para validar el formato del email
    email_regex = r"(^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$)"
    return re.match(email_regex, email) is not None

@app.route("/list_conversations", methods=["GET"])
def list_conversations():
    email = request.args.get("email")

    # Verificar si el email está presente y es válido
    if not email:
        return jsonify({"error": "Email is required"}), 400

    if not validate_email(email):
        return jsonify({"error": "Invalid email format"}), 400

    # Verificar si el usuario existe
    if not r.exists(f"user:{email}"):
        return jsonify({"error": "User not found"}), 400

    # Obtener las claves de las conversaciones asociadas al usuario
    conversation_keys = r.smembers(f"user:{email}:conversations")

    # Si no hay conversaciones, devolver una respuesta con éxito pero sin conversaciones
    conversations_data = []
    for conversation_key in conversation_keys:
        # Asegúrate de que los datos que necesitas existan en Redis
        message_count = r.hget(conversation_key, "message_count") or 0
        participants = list(r.smembers(conversation_key))
        
        conversation = {
            "conversation_key": conversation_key,
            "message_count": message_count,
            "participants": participants
        }
        conversations_data.append(conversation)

    # Si no hay conversaciones, aún se devuelve un resultado exitoso con una lista vacía
    return jsonify({"success": True, "conversations": conversations_data}), 200




if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8181)
