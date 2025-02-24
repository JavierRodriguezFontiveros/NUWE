# Use an official Python runtime as a parent image
FROM python:3.12-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
ARG FOLDER
COPY ${FOLDER} /app
RUN mkdir -p proto_generated

# Copy proto definitions into the container
COPY ../proto_definitions /app/proto_definitions

# Install required Python packages
RUN pip install --no-cache-dir -r requirements.txt

# Install netcat (netcat-openbsd) for network connectivity checks
RUN apt-get update && apt-get install -y netcat-openbsd

# Generate the Python files from the .proto files
RUN python -m grpc_tools.protoc \
    -I./proto_definitions \
    --python_out=./proto_generated \
    --grpc_python_out=./proto_generated \
    ./proto_definitions/message.proto

RUN python -m grpc_tools.protoc \
    -I./proto_definitions \
    --python_out=./proto_generated \
    --grpc_python_out=./proto_generated \
    ./proto_definitions/user.proto

RUN python -m grpc_tools.protoc \
    -I./proto_definitions \
    --python_out=./proto_generated \
    --grpc_python_out=./proto_generated \
    ./proto_definitions/notification.proto

ENV PYTHONPATH="/app/proto_generated:${PYTHONPATH}"

# Run message service (gRPC)
CMD ["python", "app.py"]
