
![certificado](image/certificado.png)


# 🛠️ Python Backend - gRPC & REST Microservices 🐍

Category   ➡️   Software

Subcategory   ➡️   Python Backend

Difficulty   ➡️   Medium

Average solution time ➡️ 4 hours. The timer will begin when you click the start button and will stop upon your submission. However, this is only a reference metric and does not impact your final score. Focus on delivering a high-quality solution

---

## 🌐 Background
In this challenge you will showcase your ability to build and debug Python-based microservices using gRPC, REST, and Redis. The system comprises four containerized services—UserService, MessageService, NotificationService, and FrontendService—designed for user management, messaging, notifications, and frontend communication.

Your tasks include: fixing inter-service communication bugs, implementing notification delivery, and ensuring smooth integration across services using shared Proto Definitions. The challenge evaluates your debugging skills, feature implementation, and code quality while adhering to the existing architecture and protocols.

## 📂 Repository Structure
The repository is organized to streamline development and testing of the four microservices. Below is the directory structure:
```

├── proto_definitions/
│   ├── user.proto
│   ├── message.proto
│   ├── notification.proto
│   └── frontend.proto
│
├── user_service/
│   ├── api_rest/
│   │   ├── app.py
│   │   └── requirements.txt
│   ├── grpc/
│   │   ├── app.py
│   │   ├── requirements.txt
│   │   └── proto_generated/
│   │       └── ...
│   └── Dockerfile
│
├── message_service/
│   ├── api_rest/
│   │   ├── app.py
│   │   └── requirements.txt
│   ├── grpc/
│   │   ├── app.py
│   │   ├── requirements.txt
│   │   └── proto_generated/
│   │       └── ...
│   └── Dockerfile
│
├── notification_service/
│   ├── api_rest/
│   │   ├── app.py
│   │   └── requirements.txt
│   ├── grpc/
│   │   ├── app.py
│   │   ├── requirements.txt
│   │   └── proto_generated/
│   │       └── ...
│   └── Dockerfile
│
├── frontend_service/
│   ├── api_rest/
│   │   ├── app.py
│   │   └── requirements.txt
│   ├── grpc/
│   │   ├── app.py
│   │   ├── requirements.txt
│   │   └── proto_generated/
│   │       └── ...
│   └── Dockerfile
│
├── docker-compose.yml
└── README.md

      
```
## 🎯 Tasks

The project will consist of 4 microservices: `UserService`, `MessageService`, `NotificationService`, and `FrontendService`. Each service will include an HTTP server and a gRPC server, except for the `FrontendService`, which will only provide a gRPC server. All services will share access to a common `proto_definitions` folder containing the Protocol Buffers (Protobuf) definitions used for gRPC communication across the project. 

You must complete the following tasks:
- **Task 1:** Fix Communication Between `MessageService` and `UserService`

There are bugs and missing statements in the internal communication between `MessageService` and `UserService` that disrupt functionality. Your task is to debug and resolve these issues to ensure smooth data flow and accurate responses.


- **Task 2:** Implement Notification-to-Frontend Communication

The `NotificationService` must forward notifications to the `FrontendService` for users subscribed to notifications. Additionally, it should manage user notification subscription statuses. Below are the steps to implement this functionality:

1. **Trigger**: A message creation event in the `MessageService` triggers a notification.
2. **Notification Creation**: If the recipient user is subscribed, the `NotificationService` creates a notification. By default, all users are subscribed upon creation.
3. **Frontend Delivery**: Use gRPC to push the notification from `NotificationService` to the `FrontendService`.
4. **Persistence**: Store the notification in memory using Redis.


####  Port Configuration

Each service is configured with unique HTTP and gRPC ports to avoid conflicts and streamline inter-service communication. Here are the expected port details for all services:

| **Service**             | **HTTP** | **gRPC** |
|-------------------------|----------|----------|
| `user-service`          | 8080     | 9797     |
| `message-service`       | 8181     | 9696     |
| `notification-service`  | 8282     | 9898     |
| `frontend-grpc-service` | ----     | 3030     |
---

| **Service**             | **Port** |
|-------------------------|----------|
| `redis`                 | 6397     |     

---

## 💫 Guides 
Below you can find a detailed description of the microservices and their functions.

### **1. User Service**

#### HTTP Endpoints:

| **Endpoint**                    | **Method** | **Params/Body**                                                                                  | **Requires Auth** | **Response Codes**              |
|----------------------------------|------------|--------------------------------------------------------------------------------------------------|-------------------|----------------------------------|
| `/users`                         | POST       | `{ "email": string, "password": string, "name": string }`                                        | No                | 201 (Created), 400 (Error)       |
| `/users/email`                   | PUT        | `{ "email": string, "data": { "email"?: string, "password"?: string, "name"?: string }}`        | No                | 200 (OK), 400 (Invalid Data), 404 (Not Found) |
| `/users/email`                   | DELETE     | `{ "email": string }`                                                                           | No                | 204 (No Content), 404 (Not Found) |

---

#### gRPC Methods:

| **Method**            | **RPC Call**         | **Request Params**                   | **Response Params**                | **Description**                     |
|-----------------------|----------------------|--------------------------------------|------------------------------------|-------------------------------------|
| `Authenticate`        | `AuthenticateUser`   | `{ "email": string, "password": string }` | `{ "email": string, "success": bool }` | Verifies user credentials.          |
| `Check Exists`        | `CheckUserExists`    | `{ "email": string }`                | `{ "exists": bool }`               | Checks if a user exists.            |
| `List Users`          | `ListUsers`          | `empty`                              | `[User]`                             | Retrieves a list of all users.      |


#### Database Structure : Redis - DB: 0 (Users)

The `db=0` database is dedicated to storing user data. Each user record is indexed by their email address and stored as a JSON string.

- **Key Pattern:** `users:{user_email}`
- **Type:** String (JSON formatted)
- **Description:** Stores detailed user information, including name, email, and password.

#### Example User JSON Structure:
```json
{
  "name": "User's Name",
  "email": "user@example.com",
  "password": "hashed_password"
}
```


### **2. Message Service**

#### HTTP Endpoints:

| **Endpoint**                    | **Method** | **Params/Body**                                                                                  | **Requires Auth** | **Response Codes**              |
|----------------------------------|------------|--------------------------------------------------------------------------------------------------|-------------------|----------------------------------|
| `/list_conversations`           | GET        | `{ "email": string }`                                                                             | No                | 200 (OK), 400 (Bad Request)     |

---

#### gRPC Methods:
| **Method**            | **RPC Call**         | **Request Params**                     | **Response Params**                        | **Description**                     |
|-----------------------|----------------------|----------------------------------------|-------------------------------------------|-------------------------------------|
| `SendMessage`         | `SendMessage`        | `{ "message": { sender_email: string, receiver_email: string, content: string } }` | `{ success: bool, message: string }`      | Sends a message from sender to receiver. |
| `GetMessages`         | `GetMessages`        | `{ "user_email": string }`             | `{ messages: [Message] }`                 | Retrieves all messages for the given user. |


#### Database Structure : Redis - DB: 1 (Messages & Conversations)

The `db=1` Redis database stores both messages and conversation metadata.

- **Key Pattern:** `conversation:{user_email_1}:{user_email_2}`
    - **Type:** Hash
    - **Description:** Stores conversation data for specific pairs of users.
    - **Example Key:**`
    - `conversation:sender@example.com:receiver@example.com`
  
- **Key Pattern for Messages:** `message:{message_id}`
  - **Type:** String (JSON formatted)
  - **Description:** Stores individual message details such as sender, receiver, content, and timestamp.   
  ```json
    {
      "id": 1,
      "sender_email": "sender@example.com",
      "receiver_email": "receiver@example.com",
      "content": "Hello, how are you?",
      "timestamp": "2025-01-14T12:00:00Z"
    }
  ```
- **Additional Key:** `message_id_counter`

- **Key Pattern for Users' Conversations:** `user:{user_email}:conversations`
    - **Type:** Set
    - **Description:** Tracks all the conversation keys for a specific user.
    - **Example Keys:**
    - `user:notification_sender@example.com:conversations`




### **3. Notification Service**

#### HTTP Endpoints:

| **Endpoint**                    | **Method** | **Params/Body**                                                                                  | **Requires Auth** | **Response Codes**              |
|----------------------------------|------------|--------------------------------------------------------------------------------------------------|-------------------|----------------------------------|
| `/list_notifications`           | GET        | `{ "email": string }`                                                                             | No                | 200 (OK), 400 (Bad Request)     |

---

#### gRPC Methods:
| **Method**            | **RPC Call**         | **Request Params**                     | **Response Params**                        | **Description**                     |
|-----------------------|----------------------|----------------------------------------|-------------------------------------------|-------------------------------------|
| `Create notification`         | `CreateNotification`        | `{ sender_email: string, receiver_email:string} }` | `{ success: bool }`      | Creates a notification for the receiver. Notification should have a `'read': bool` parameter. |
| `Check user subscription`         | `CheckUserSubscribed`        | `{ "email": string }`             | `{ subscribed: bool }`                 | Checks if a user is subscribed to the notifications. |
| `Subscribe a User`         | `SubscribeUser`        | `{ "email": string }`             | `{ sucess: bool }`                 | Subscribes a user to the notification service. |
| `Unsubscribe a User`         | `UnsubscribeUser`        | `{ "email": string }`             | `{ sucess: bool }`                 | Unsubscribes a user to the notification service. |

#### Database Structure : Redis - DB: 2 (Notifications & Notifications' subscription)

The `db=2` Redis database stores both notification and notifications' subscriptions metadata.

- **Key Pattern Notifications:** `notifications:{user_email}`
- **Type:** List
- **Description:** Stores notification data for each user. Each notification is stored as a JSON object containing details such as the sender, receiver, timestamp, and read status.
- **Structure of Notification (JSON):**
  ```json
  {
    "sender_email": "bob@example.com",
    "receiver_email": "asdf@example.com",
    "timestamp": "2025-01-13T19:31:14.207231",
    "read": false
  }
  ```

- **Key Pattern Subscriptions:** `subscriptions`
- **Type:** Hash
- **Description:** Stores subscription details for various users. Each field in the hash represents a user's email as the key and their subscription details as the value.
- **Structure of value (JSON):**
  ```json
  {
    "user_email": "notification_sender@example.com", "subscribed": true
  }
  ```

### **4. Frontend Service**



#### gRPC Methods:
| **Method**            | **RPC Call**         | **Request Params**                     | **Response Params**                        | **Description**                     |
|-----------------------|----------------------|----------------------------------------|-------------------------------------------|-------------------------------------|
| `Receive Notifications`         | `ReceiveNotification`        | `[Notification]` | `{ success: bool }`      | Receives a notification from NotificationService. |



### 🏃‍♂️ How to Run
Docker is used to containerize and run the project efficiently. You are provided with a `docker-compose.yml` file that **MUST NOT BE MODIFIED** . Additionally, each service has its own `Dockerfile`, which can be modified if needed, although changes are not required to solve the challenge.

Follow these steps to ensure everything runs smoothly:

1. Verify Docker installation:

- Make sure Docker and Docker Compose are installed on your system. You can verify this by running `docker --version` `docker compose version`. If not installed, refer to [Docker's installation guide](https://docs.docker.com/engine/install/).

2. Build the project:

- Run the following command to build all the necessary Docker images: `docker compose build` . This step will pull the required base images, install dependencies, and prepare the environment for each service.
- Start the containers in detached mode with `docker compose up -d`. 
This command starts all the services defined in the `docker-compose.yml` file in the background.
Monitor the containers (optional):


**Additional Notes:**
- **Proto Definitions:** Ensure that the proto definitions are located in the correct folder as specified in the project structure.
- **Proto Invocations:** You must generate the proto invocations before running the project. 

### ✅ How to Test
Example test cases are provided in the `tests/` folder.
You are encouraged to implement your own tests for additional scenarios.
- **Please note:**
The provided test structure serves as a guideline.
The actual tests used for evaluation will differ and will not be shared in advance.


## 📤 Submission
To successfully submit your solution, follow these steps:

1. Complete the proposed tasks.
2. Continuously push your changes as you work.
3. Monitor your progress and wait for the results, it may take up to a few minutes.
4. Once satisfied with your solution, click the "Submit Challenge" button.

#### Restrictions:
- Do NOT modify the `docker-compose.yml` file.
- Do NOT change the main challenge folders structure. However, you are free to make structural changes within each microservice as needed.



## 📊 Evaluation

The final score will be given according to whether or not the objectives have been met.

In this case, the challenge will be evaluated on 1900 (1500 for tasks and 400 for code quality) points which are distributed as follows:

- Task 1: 650 points
- Task 2: 850 points
- Code quality: 400 points

## ❓ Additional information
**Q1: Can I change anything in the app?**

A1: Yes, as the app is dockerised, you are free to modify anything within the project. But keep in mind that the ports and `docker-compose.yml` must not be modified. 

**Q2: Can I add resources that are not in requirements.txt?**

A2: Yes, new resources have to be added if necessary. Remember to add them to the `requirements.txt` files.

**Q3: What happens if I accidentally change the `docker-compose.yml` file?**

A3: The `docker-compose.yml` file must remain unchanged. If modified, it might lead to evaluation errors. Revert any unintended changes before submitting.