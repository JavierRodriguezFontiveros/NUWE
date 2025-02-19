provider "aws" {
  access_key                  = "test"
  secret_key                  = "test"
  region                      = "us-east-1"
  skip_credentials_validation = true
  skip_metadata_api_check     = true
  skip_requesting_account_id  = true

  endpoints {
    apigateway   = "http://localhost:4566"
    lambda       = "http://localhost:4566"
    dynamodb     = "http://localhost:4566"
    iam          = "http://localhost:4566"
  }
}

# DynamoDB Table
resource "aws_dynamodb_table" "eventos" {
  name         = "EVENTOS"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "eventId"

  attribute {
    name = "eventId"
    type = "S"
  }
}

# IAM Role for Lambda
resource "aws_iam_role" "lambda_role" {
  name = "lambda_execution_role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {"Service": "lambda.amazonaws.com"},
      "Effect": "Allow"
    }
  ]
}
EOF
}

# IAM Policy for Lambda to write in DynamoDB
resource "aws_iam_policy" "dynamodb_policy" {
  name        = "DynamoDBWritePolicy"
  description = "Allows Lambda to write to DynamoDB"

  policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "dynamodb:PutItem",
      "Resource": "${aws_dynamodb_table.eventos.arn}"
    }
  ]
}
EOF
}

# Attach policy to Lambda Role
resource "aws_iam_role_policy_attachment" "lambda_policy_attach" {
  role       = aws_iam_role.lambda_role.name
  policy_arn = aws_iam_policy.dynamodb_policy.arn
}

# Lambda Function
resource "aws_lambda_function" "create_event_handler" {
  function_name    = "CreateEventHandler"
  runtime         = "python3.8"
  handler         = "createevent.lambda_handler"
  role            = aws_iam_role.lambda_role.arn
  filename        = "../lambda/createevent.zip"
  source_code_hash = filebase64sha256("../lambda/createevent.zip")
}

# API Gateway REST API
resource "aws_api_gateway_rest_api" "api" {
  name        = "producto-nuevo"
  description = "API Gateway para gestionar eventos"
}

# API Gateway Resource
resource "aws_api_gateway_resource" "eventos" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  parent_id   = aws_api_gateway_rest_api.api.root_resource_id
  path_part   = "eventos"
}

# API Gateway Method
resource "aws_api_gateway_method" "post_eventos" {
  rest_api_id   = aws_api_gateway_rest_api.api.id
  resource_id   = aws_api_gateway_resource.eventos.id
  http_method   = "POST"
  authorization = "NONE"
}

# API Gateway Integration with Lambda
resource "aws_api_gateway_integration" "lambda_integration" {
  rest_api_id = aws_api_gateway_rest_api.api.id
  resource_id = aws_api_gateway_resource.eventos.id
  http_method = aws_api_gateway_method.post_eventos.http_method
  integration_http_method = "POST"
  type = "AWS_PROXY"
  uri  = aws_lambda_function.create_event_handler.invoke_arn
}

# Deploy API Gateway
resource "aws_api_gateway_deployment" "deployment" {
  depends_on  = [aws_api_gateway_integration.lambda_integration]
  rest_api_id = aws_api_gateway_rest_api.api.id
  stage_name  = "produccion"
}

# Lambda Permission for API Gateway
resource "aws_lambda_permission" "apigw_lambda" {
  statement_id  = "AllowExecutionFromAPIGateway"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.create_event_handler.function_name
  principal     = "apigateway.amazonaws.com"
  source_arn    = "${aws_api_gateway_rest_api.api.execution_arn}/*/*"
}
