import json
import boto3
import logging

s3_client = boto3.client('s3')
sns_client = boto3.client('sns')

# Configuración para los servicios
s3_bucket = "datastorage"
sns_topic_arn = "arn:aws:sns:us-east-1:000000000000:notificaciones"  # Asegúrate de que el ARN es correcto

def lambda_handler(event, context):
    for record in event['Records']:
        try:
            # Decodificar el mensaje desde Kinesis
            payload = json.loads(record['kinesis']['data'])
            
            # Modificar los datos para agregar la clave "test" con valor True
            payload['test'] = True

            # Crear el nombre del archivo basado en el eventID
            event_id = record['eventID']
            filename = f"{event_id}.json"
            
            # Subir el archivo modificado a S3
            s3_client.put_object(
                Bucket=s3_bucket,
                Key=filename,
                Body=json.dumps(payload)
            )
            print(f"Archivo subido exitosamente a S3: {filename}")

        except Exception as e:
            # En caso de error, enviar mensaje a SNS
            error_message = f"Error procesando el evento {record['eventID']}: {str(e)}"
            sns_client.publish(
                TopicArn=sns_topic_arn,
                Message=error_message,
                Subject="Error en procesamiento de Kinesis"
            )
            print(f"Error en procesamiento, mensaje enviado a SNS: {error_message}")
            
    return {
        'statusCode': 200,
        'body': json.dumps('Procesamiento completado')
    }
