# Use an official Python runtime as a parent image
FROM python:3.12-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
ARG FOLDER=notification_service/grpc
COPY ${FOLDER} /app
RUN mkdir -p proto_generated

# Copy proto definitions into the container
COPY ../proto_definitions /app/proto_definitions

# Install required Python packages
RUN pip install --no-cache-dir -r requirements.txt

# Generate the Python files from the .proto files
RUN python -m grpc_tools.protoc \
    -I./proto_definitions \
    --python_out=./proto_generated \
    --grpc_python_out=./proto_generated \
    ./proto_definitions/notification.proto

RUN python -m grpc_tools.protoc \
    -I./proto_definitions \
    --python_out=./proto_generated \
    --grpc_python_out=./proto_generated \
    ./proto_definitions/frontend.proto

# Set the PYTHONPATH for grpc generated files
ENV PYTHONPATH="/app/proto_generated:${PYTHONPATH}"

# Expose the gRPC port
EXPOSE 9898

# Run notification service (gRPC)
CMD ["python", "app.py"]
