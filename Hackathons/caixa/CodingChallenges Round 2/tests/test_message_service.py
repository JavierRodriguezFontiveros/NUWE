import requests
import grpc
import os
import subprocess

# Constants
HTTP_MESSAGE_SERVICE = "http://localhost:8181"
GRPC_MESSAGE_SERVER = "localhost:9696"


# tests message service connections


def test_create_protobuf_message_service():
    command = [
        "python",
        "-m",
        "grpc_tools.protoc",
        "--proto_path=proto_definitions/",
        "--python_out=.",
        "--grpc_python_out=.",
        "proto_definitions/message.proto",
    ]
    subprocess.run(command)

    file_exists = os.path.exists("message_pb2.py") and os.path.exists(
        "message_pb2_grpc.py"
    )
    assert file_exists


def test_connection_message_service_http():
    try:
        response = requests.get(HTTP_MESSAGE_SERVICE)
        assert True

    except Exception as e:
        raise e


def test_connection_message_service_grpc():
    try:
        with grpc.insecure_channel(GRPC_MESSAGE_SERVER) as channel:
            grpc.channel_ready_future(channel).result(timeout=3)
    except Exception as e:
        raise e


# Test send message. Assumes user_service works and the users needed have been created.
def test_send_message_successfully():
    try:
        import message_pb2
        import message_pb2_grpc

        with grpc.insecure_channel(GRPC_MESSAGE_SERVER) as channel:
            stub = message_pb2_grpc.MessageServiceStub(channel)
            request = message_pb2.SendMessageRequest(
                message=message_pb2.Message(
                    sender_email="sender@example.com",
                    receiver_email="receiver@example.com",
                    content="Hello!",
                )
            )
            response = stub.SendMessage(request)
            assert response.success
    except Exception as e:
        raise e


def test_list_conversations():
    response = requests.get(
        HTTP_MESSAGE_SERVICE + "/list_conversations?email=sender@example.com"
    )
    assert response.status_code == 200
    data = response.json()
    assert data.get("success") is True
    assert isinstance(data.get("conversations"), list)
