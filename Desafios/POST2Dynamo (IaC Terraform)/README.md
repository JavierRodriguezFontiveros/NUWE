# NUWE Cloud AWS Challenge - ALD

### Category   ➡️   Cloud AWS

### Subcategory   ➡️   IaC Terraform

### Difficulty   ➡️   (Basic)

## 🌐 Background

Some processes that are done in different environments, can consume too much time of a person doing a job that could be automated. An example of this is automatically inserting data into a NoSQL database such as DynamoDB.  

The goal of this challenge will be to develop a fully automated infrastructure that, through a simple POST Request to an endpoint, automatically inserts data into DynamoDB. All this app must be **serverless**, and developed using **Terraform** as IaC.

## ❓ Guides

- Resources to be deployed: **API Gateway**, **Lambda**, **DynamoDB**. Everything must have the necessary resources to function correctly.

- The correction of this challenge will be done in an automated way, so meeting the objectives is crucial. To this end, some naming guidelines will be given so that the correct functioning of the infrastructure can be tested:
    - Name of the table: **EVENTS**
        - Fields the table must have: **eventId** and **category**. 
    - Name of the lambda function: **CreateEventHandler**. This will automatically execute when a request is sent to the API endpoint and inserts the data into DynamoDB.
        - How the lambda function works:
            - Triggered when a valid POST request is made to the API endpoint.
            - If the request is valid, it returns: 'body': '{"status":"Event created"}' and inserts the data into DynamoDB table.
            - If not valid, it returns: '{"status":"Server Error."}'
    - Endpoint name: **/events**
    - API name: **product-nuwe**
    - API Stage: **production** 

- Development environment: Localstack. In order for the correction to be carried out, it will be necessary to develop everything for localstack, since it does not require personal keys of any kind. Some data to take into account:
    - Region: us-east-1
    - access_key: test
    - secret_key: test

- **Additional information: It is important to respect the guidelines that have been provided, as the automatic correction tests the correct performance of the infrastructure, dividing this functioning into objectives from the simplest to the most complex.**

## 🎯 Objectives

1. The main.tf file is working and ready for `apply`.
2. Deploy all proposed resources.
3. API works properly.
4. Lambda works properly.

## 📂 Repository Structure
```bash
nuwe-cloud-ald/
├── README.md
└── ServerlessApp
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

### Additional information
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

