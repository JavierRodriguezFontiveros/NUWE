import boto3
import time

# Crear cliente para SNS
sns = boto3.client('sns', region_name='us-east-1')

# Crear cliente para SQS
sqs = boto3.client('sqs', region_name='us-east-1')

# Crear un tópico SNS
topic = sns.create_topic(Name='TestTopic')

# Crear una cola SQS
queue = sqs.create_queue(QueueName='TestQueue')

# Suscribir la cola SQS al tópico SNS
sns.subscribe(
    TopicArn=topic['TopicArn'],
    Protocol='sqs',
    Endpoint=queue['QueueUrl']
)

# Publicar un mensaje en el tópico SNS
sns.publish(
    TopicArn=topic['TopicArn'],
    Message='Test message to SNS and SQS'
)

# Recibir mensajes de SQS
time.sleep(5)  # Esperar a que el mensaje sea recibido
messages = sqs.receive_message(
    QueueUrl=queue['QueueUrl'],
    MaxNumberOfMessages=1
)
print("Mensajes de SQS:", messages)
