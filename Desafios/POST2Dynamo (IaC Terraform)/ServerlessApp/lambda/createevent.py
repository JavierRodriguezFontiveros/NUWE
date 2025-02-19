import json
import boto3
from botocore.exceptions import ClientError

# Crear cliente de DynamoDB
dynamodb = boto3.resource('dynamodb', region_name='us-east-1')
table = dynamodb.Table('EVENTOS')

def lambda_handler(event, context):
    # Verificar si el cuerpo de la solicitud tiene la información esperada
    try:
        body = json.loads(event['body'])
        event_id = body['eventId']
        category = body['category']
        
        # Insertar los datos en DynamoDB
        response = table.put_item(
            Item={
                'eventId': event_id,
                'category': category
            }
        )

        # Respuesta exitosa
        return {
            'statusCode': 200,
            'body': json.dumps({'status': 'Evento creado'})
        }

    except Exception as e:
        # Responder con un error si algo falla
        return {
            'statusCode': 500,
            'body': json.dumps({'status': 'Error de servidor'})
        }

# gola