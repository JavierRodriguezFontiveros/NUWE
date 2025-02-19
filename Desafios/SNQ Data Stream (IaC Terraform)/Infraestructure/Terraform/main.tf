# 1. Definir un único proveedor AWS para LocalStack
provider "aws" {
  access_key                  = "prueba"
  secret_key                  = "prueba"
  region                      = "us-east-1"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  # Endpoints para las configuraciones de recursos
  endpoints {
    apigateway     = "http://localhost:4566"
    apigatewayv2   = "http://localhost:4566"
    cloudformation = "http://localhost:4566"
    cloudwatch     = "http://localhost:4566"
    dynamodb       = "http://localhost:4566"
    ec2            = "http://localhost:4566"
    es             = "http://localhost:4566"
    elasticache    = "http://localhost:4566"
    firehose       = "http://localhost:4566"
    iam            = "http://localhost:4566"
    kinesis        = "http://localhost:4566"
    lambda         = "http://localhost:4566"
    rds            = "http://localhost:4566"
    redshift       = "http://localhost:4566"
    route53        = "http://localhost:4566"
    s3             = "http://localhost:4566"
    secretsmanager = "http://localhost:4566"
    ses            = "http://localhost:4566"
    sns            = "http://localhost:4566"
    sqs            = "http://localhost:4566"
    ssm            = "http://localhost:4566"
    stepfunctions  = "http://localhost:4566"
    sts            = "http://localhost:4566"
  }
}

# 2. Definir la infraestructura
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

# 3. Crear el Kinesis Stream
resource "aws_kinesis_stream" "datastream" {
  name             = "datastream"
  shard_count      = 1
  retention_period = 24
}

# 4. Crear el Bucket S3
resource "aws_s3_bucket" "datastorage" {
  bucket = "datastorage"
}

# 5. Crear el SNS Topic para notificaciones
resource "aws_sns_topic" "notificaciones" {
  name = "notificaciones"
}

# 6. Crear la SQS Queue para recibir mensajes de SNS
resource "aws_sqs_queue" "sqs_for_sns" {
  name = "sqs_for_sns"
}

# 7. Suscribir SQS a SNS para recibir notificaciones
resource "aws_sns_topic_subscription" "sns_to_sqs" {
  topic_arn = aws_sns_topic.notificaciones.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.sqs_for_sns.arn
}

# 8. Crear un rol IAM para la Lambda (permite acceder a Kinesis, S3, SNS)
resource "aws_iam_role" "lambda_exec" {
  name = "lambda_exec_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "lambda.amazonaws.com"
        }
      }
    ]
  })
}

# 9. Crear la política de acceso para la Lambda (acceso a Kinesis, S3, SNS)
resource "aws_iam_policy" "lambda_policy" {
  name        = "lambda_policy"
  description = "Permite a Lambda acceder a Kinesis, S3 y SNS"
  policy      = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action   = ["s3:PutObject"]
        Resource = "${aws_s3_bucket.datastorage.arn}/*"
        Effect   = "Allow"
      },
      {
        Action   = "kinesis:GetRecords"
        Resource = aws_kinesis_stream.datastream.arn
        Effect   = "Allow"
      },
      {
        Action   = "sns:Publish"
        Resource = aws_sns_topic.notificaciones.arn
        Effect   = "Allow"
      }
    ]
  })
}

# 10. Adjuntar la política al rol de Lambda
resource "aws_iam_role_policy_attachment" "lambda_policy_attachment" {
  role       = aws_iam_role.lambda_exec.name
  policy_arn = aws_iam_policy.lambda_policy.arn
}

# 11. Crear la Lambda function (datamanipulation)
resource "aws_lambda_function" "datamanipulation" {
  function_name = "datamanipulation"
  runtime       = "python3.8"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "lambda_function.lambda_handler"
  filename      = "Infraestructure/lambda/lambda.zip"
}

# 12. Crear la configuración de Kinesis como disparador de Lambda
resource "aws_lambda_event_source_mapping" "kinesis_to_lambda" {
  event_source_arn = aws_kinesis_stream.datastream.arn
  function_name    = aws_lambda_function.datamanipulation.arn
  starting_position = "LATEST"
}
