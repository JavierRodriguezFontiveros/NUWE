import json
import redis
from flask import Flask, request, jsonify
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)
redis_host = "redis"
redis_port = 6379
r = redis.StrictRedis(host=redis_host, port=redis_port, db=0, socket_connect_timeout=5)

def hash_password(password):
    return generate_password_hash(password)

def verify_password(stored_password, password):
    return check_password_hash(stored_password, password)

@app.route("/users", methods=["POST"])
def create_user():
    data = request.get_json()

    email = data.get("email")
    name = data.get("name")
    password = data.get("password")

    if not email or not name or not password:
        return jsonify({"message": "Invalid data, please provide email, name and password"}), 400

    if r.exists(f"user:{email}"):
        return jsonify({"message": "User already exists"}), 400

    hashed_password = hash_password(password)
    user_data = {
        "email": email,
        "name": name,
        "password": hashed_password
    }
    r.set(f"user:{email}", json.dumps(user_data))

    return jsonify({"message": "User created successfully"}), 201



@app.route("/users/<email>", methods=["GET"])
def check_user_exists(email):
    if r.exists(f"user:{email}"):
        return jsonify({"exists": True}), 200
    return jsonify({"exists": False, "message": "User not found"}), 404


@app.route("/users/<email>", methods=["DELETE"])
def delete_user(email):
    if not r.exists(f"user:{email}"):
        return jsonify({"message": "User not found"}), 404

    # Eliminar usuario y conversaciones asociadas
    r.delete(f"user:{email}")
    r.delete(f"user:{email}:conversations")

    # Eliminar todas las conversaciones asociadas al usuario
    conversation_keys = r.smembers(f"user:{email}:conversations")
    for conversation_key in conversation_keys:
        r.delete(conversation_key)

    return jsonify({"message": f"User {email} deleted successfully"}), 204



@app.route("/users/<email>", methods=["PUT"])
def update_user(email):
    # Verificar si el usuario existe
    if not r.exists(f"user:{email}"):
        return jsonify({"message": "User not found"}), 404  # Si no existe, retornamos 404

    # Obtener los datos enviados en el body
    data = request.get_json()

    # Verificar que 'data' contenga algo
    if not data:
        return jsonify({"message": "No data provided to update"}), 400  # Si no hay datos, retornamos 400

    # Validar que al menos uno de los campos 'name', 'password' esté presente
    if not any(key in data for key in ["name", "password"]):
        return jsonify({"message": "No valid fields to update, provide at least one of 'name' or 'password'"}), 400

    # Cargar los datos actuales del usuario
    user_data = json.loads(r.get(f"user:{email}"))

    # Validar el campo 'name' si está presente
    if "name" in data:
        if not data["name"]:
            return jsonify({"message": "'name' cannot be empty"}), 400  # Si 'name' es vacío, retornamos 400
        user_data["name"] = data["name"]

    # Validar el campo 'password' si está presente
    if "password" in data:
        password = data["password"]
        if len(password) < 6:  # Puedes cambiar la longitud mínima de la contraseña según tus requisitos
            return jsonify({"message": "Password is too short, must be at least 6 characters"}), 400  # Validación de contraseña
        user_data["password"] = hash_password(password)

    # Guardar los datos actualizados en Redis
    r.set(f"user:{email}", json.dumps(user_data))

    return jsonify({"message": f"User {email} updated successfully"}), 200



if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=8080)

