import requests
import grpc
import os
import subprocess

# Constants
HTTP_USER_SERVICE = "http://localhost:8080/users"
HTTP_MESSAGE_SERVICE = "http://localhost:8181"
HTTP_NOTIFICATION_SERVICE = "http://localhost:8282"

GRPC_USER_SERVER = "localhost:9797"
GRPC_MESSAGE_SERVER = "localhost:9696"
GRPC_NOTIFICATION_SERVER = "localhost:9898"
GRPC_FRONTEND_SERVER = "localhost:3030"


# Test notification service connections
def test_create_protobuf_notification_service():
    command = [
        "python",
        "-m",
        "grpc_tools.protoc",
        "--proto_path=proto_definitions/",
        "--python_out=.",
        "--grpc_python_out=.",
        "proto_definitions/notification.proto",
    ]
    subprocess.run(command)

    file_exists = os.path.exists("notification_pb2.py") and os.path.exists(
        "notification_pb2_grpc.py"
    )
    assert file_exists


def test_connection_notification_service_http():
    try:
        response = requests.get(HTTP_NOTIFICATION_SERVICE)
        assert True  # If an error has not been raised, port is working
    except Exception as e:
        raise e


def test_connection_notification_service_grpc():
    try:
        with grpc.insecure_channel(GRPC_NOTIFICATION_SERVER) as channel:
            grpc.channel_ready_future(channel).result(timeout=3)
    except Exception as e:
        raise e


# check frontend definition
def test_create_protobuf_frontend_service():
    command = [
        "python",
        "-m",
        "grpc_tools.protoc",
        "--proto_path=proto_definitions/",
        "--python_out=.",
        "--grpc_python_out=.",
        "proto_definitions/frontend.proto",
    ]
    subprocess.run(command)

    file_exists = os.path.exists("frontend_pb2.py") and os.path.exists(
        "frontend_pb2_grpc.py"
    )
    assert file_exists


# check frontend grpc connection
def test_connection_frontend_service_grpc():
    try:
        with grpc.insecure_channel(GRPC_FRONTEND_SERVER) as channel:
            grpc.channel_ready_future(channel).result(timeout=3)
    except Exception as e:
        raise e


def test_create_notification():
    try:
        import notification_pb2
        import notification_pb2_grpc

        notification_channel = grpc.insecure_channel(GRPC_NOTIFICATION_SERVER)
        notification_stub = notification_pb2_grpc.NotificationServiceStub(
            notification_channel
        )

        for notification in [
            {
                "sender_email": "sender@example.com",
                "receiver_email": "receiver@example.com",
            },
            {
                "sender_email": "sender@example.com",
                "receiver_email": "alice@example.com",
            },
        ]:
            notification_response = notification_stub.CreateNotification(
                notification_pb2.CreateNotificationRequest(
                    sender_email=notification["sender_email"],
                    receiver_email=notification["receiver_email"],
                )
            )
            assert notification_response.success

    except Exception as e:
        raise e


# Check list notifications works
def test_list_notifications():
    user_email = "alice@example.com"
    response = requests.get(
        HTTP_NOTIFICATION_SERVICE + "/list_notifications",
        params={"email": user_email},
    )
    assert response.status_code == 200
    data = response.json()
    assert data.get("success") is True
    assert isinstance(data.get("notifications"), list)
