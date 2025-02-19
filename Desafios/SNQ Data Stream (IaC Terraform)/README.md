# Proyecto Terraform

## Requisitos
- Terraform instalado (versión recomendada: 1.x)
- AWS CLI configurado (si aplicas recursos en AWS)

## Pasos para aplicar la infraestructura

1. Inicializar Terraform:
   ```bash
   terraform init
   ```

2. Aplicar los recursos:
   ```bash
   terraform apply -auto-approve
   ```

3. Verificar la infraestructura en AWS (u otro proveedor).

## Pasos para destruir la infraestructura

1. Ejecutar el siguiente comando:
   ```bash
   terraform destroy -auto-approve
   ```



# NUWE Cloud AWS Challenge - KSS

### Category   ➡️   Cloud AWS

### Subcategory   ➡️   IaC Terraform

### Difficulty   ➡️   (Basic)

## 🌐 Background

When you have a constant flow of data on which a modification has to be made, it is important to monitor its proper functioning, which cannot be done manually, it has to be automated. The objective is to create an infrastructure capable of receiving JSON data(Kinesis), modifying it and sending notifications in case of malfunction. All this app must be **serverless**, and developed using **Terraform** as IaC.

## ❓ Guides

- Resources to be deployed: **S3**, **Lambda**, **Kinesis**, **SNS**, **SQS**. Everything must have the necessary resources to function correctly.

- The correction of this challenge will be done in an automated way, so meeting the objectives is crucial. To this end, some naming guidelines will be given so that the correct functioning of the infrastructure can be tested:
    - Name of the S3 Bucket: **datastorage** 
    - Name of the lambda function: **datamanipulation**
        - How the lambda function works: 
            - Triggered when inserting data into Kinesis.
            - Iterate over the records passed to the lambda function.
            - Modify data to add the key "test" with value True.
            - Create a filename for the S3 object using eventID of the current record.
            - The JSON output file is loaded into the S3 bucket.
            - If an error occurs, it is caught and an SNS topic is published.
    - Data stream name: **datastream**
    - SQS name: **sqs_for_sns**. Used to view the SNS error message.
    - SNS Name: **notifications**.

- Development environment: Localstack. In order for the correction to be carried out, it will be necessary to develop everything for localstack, since it does not require personal keys of any kind. Some data to take into account:
    - Region: us-east-1
    - access_key: test
    - secret_key: test

- **Additional information: It is important to respect the guidelines that have been provided, as the automatic correction tests the correct performance of the infrastructure, dividing this functioning into objectives from the simplest to the most complex.**

## 🎯 Objectives

1. The main.tf file is working and ready for `apply`.
2. Deploy all proposed resources.
3. Kinesis and Lambda work properly.
4. SNS & SQS work properly on errors.

## 📂 Repository Structure
```bash
nuwe-cloud-kss/
├── README.md
└── Infraestructure
    ├── lambda
    │   ├── lambdaname.py
    │   └── lambda.zip
    └── Terraform
        ├── main.tf
        ├── policy.json
        └── Other files required by main.tf
```
**The structure predefined in the challenge must always be followed for the automatic correction to work correctly. This structure and the names may vary, but it will always follow a standard that cannot be modified by the participant.**

### Modifiable files
- lambdaname.py: contains the logic of the lambda function in Python.
- main.tf: contains the logic of the infrastructure to be developed by the participant.
- policy.json(only if needed): file necessary for the correct functioning of the logic applied in main.tf. 
- Other files: the files that are necessary/required by main.tf for the infrastructure to work properly can be added.

### Información adicional
- The lambda function must be written in python.

## 📊 Evaluation
The final score will be given on the basis of whether or not the objectives have been met.

In this case, the challenge will be evaluated on 900 points which are distributed as follows:

- Objective 1: 225 points
- Objective 2: 225 points
- Objective 3: 225 points
- Objective 4: 225 points

### 📤 Submission

1. Solve the proposed objectives.
2. Push the changes you have made.
3. Click on Submit Challenge.
4. Wait for the results.