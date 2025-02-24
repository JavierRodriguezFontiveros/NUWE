from flask import Flask, request, jsonify
import redis
import json

app = Flask(__name__)
r = redis.StrictRedis(host="redis", port=6379, db=2, decode_responses=True)

@app.route("/list_notifications", methods=["GET"])
def list_notifications():
    email = request.args.get("email")
    if not email:
        return jsonify({"error": "Email is required"}), 400

    notifications_key = f"notifications:{email}"
    notifications = [json.loads(n) for n in r.lrange(notifications_key, 0, -1)]

    return jsonify({"success": True, "notifications": notifications}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8282, debug=True)
