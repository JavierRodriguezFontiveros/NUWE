import grpc
from concurrent import futures
from proto_generated import user_pb2
from proto_generated import user_pb2_grpc
import redis
import json
from werkzeug.security import generate_password_hash, check_password_hash

redis_host = "redis"
redis_port = 6379
r = redis.StrictRedis(host=redis_host, port=redis_port, db=0, socket_connect_timeout=5)

def hash_password(password):
    return generate_password_hash(password)

def verify_password(stored_password, password):
    return check_password_hash(stored_password, password)

class UserService(user_pb2_grpc.UserServiceServicer):
    def AuthenticateUser(self, request, context):
        email = request.email
        password = request.password

        if not r.exists(f"user:{email}"):
            context.set_details("User not found")
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return user_pb2.AuthenticateUserResponse(email=email, success=False)

        stored_password = r.hget(f"user:{email}", "password")
        if not verify_password(stored_password, password):
            context.set_details("Invalid password")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return user_pb2.AuthenticateUserResponse(email=email, success=False)

        return user_pb2.AuthenticateUserResponse(email=email, success=True)

    def CheckUserExists(self, request, context):
        email = request.email
        exists = r.exists(f"user:{email}")
        return user_pb2.CheckUserExistsResponse(exists=exists)

    def ListUsers(self, request, context):
        cursor = "0"
        users = []
        while cursor != "0":
            cursor, all_keys = r.scan(cursor=cursor, match="user:*")
            for key in all_keys:
                user_data = json.loads(r.get(key).decode("utf-8"))
                user = user_pb2.User(
                    email=user_data["email"],
                    name=user_data["name"],
                    password=user_data["password"]
                )
                users.append(user)
        return user_pb2.ListUsersResponse(users=users)



    def DeleteUser(self, request, context):
        email = request.email
        if not r.exists(f"user:{email}"):
            context.set_details("User not found")
            context.set_code(grpc.StatusCode.NOT_FOUND)
            return user_pb2.DeleteUserResponse(success=False)

        r.delete(f"user:{email}")
        r.delete(f"user:{email}:conversations")

        return user_pb2.DeleteUserResponse(success=True)

    def CreateUser(self, request, context):
        email = request.email
        name = request.name
        password = request.password

        if not email or not name or not password:
            context.set_details("Invalid data")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return user_pb2.CreateUserResponse(success=False)

        if r.exists(f"user:{email}"):
            context.set_details("User already exists")
            context.set_code(grpc.StatusCode.ALREADY_EXISTS)
            return user_pb2.CreateUserResponse(success=False)

        hashed_password = hash_password(password)
        user_data = {
            "email": email,
            "name": name,
            "password": hashed_password
        }
        r.set(f"user:{email}", json.dumps(user_data))

        return user_pb2.CreateUserResponse(success=True)


def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    user_pb2_grpc.add_UserServiceServicer_to_server(UserService(), server)
    server.add_insecure_port("[::]:9797")
    server.start()
    server.wait_for_termination()

if __name__ == "__main__":
    serve()
