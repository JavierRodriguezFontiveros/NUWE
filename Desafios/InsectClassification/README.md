
![certificado](predictions/certificado.png)

# 🐞 Insect Classification 🗃️

Category   ➡️   Data Science

Subcategory   ➡️   Machine Learning Engineer

Difficulty   ➡️   Easy

Expected solution time ➡️ 3 hours. **It is essential to complete your solution within this timeframe,** as it is a critical performance indicator used by the hiring company to evaluate your work. The timer will begin when you click the start button and will stop upon your submission.

---

## 🌐 Background

In an era where biodiversity conservation is becoming increasingly critical, leveraging data science to monitor and protect insect populations is vital. This project presents an **engaging opportunity to contribute to environmental science** by predicting the category of insects based on various sensor data inputs.

The challenge is straightforward but carries substantial ecological importance. We aim to **<u>classify the type of insect using sensor data</u>** from different habitats. Accurate classification can inform better strategies for insect conservation and help maintain the balance of ecosystems.

![Image](https://cdn.nuwe.io/infojobs-data/__images/ML1_FullPipeline.png)


### 🗂️ Dataset 

You will work with the provided CSV files, `train.csv` and `test.csv`. The **train dataset** contains several sensor readings along with the corresponding insect category. The **test dataset** includes similar sensor data but without the insect category, which your model should predict.

The datasets are structured as follows:

- `Sensor_alpha`, `Sensor_beta`, `Sensor_gamma`: Continuous variables from sensors indicating environmental conditions.
- `Hour`, `Minutes`: Time variables for when the sensor readings were taken.
- `Insect`: Categorical variable indicating the insect classification, present only in the training set.

Ensure to consider all the sensor features for classification as they collectively contribute to identifying the correct insect category.

### 📊 Data Processing

Data preprocessing should be applied to normalize or scale the continuous sensor variables.

### 🤖 Model

You are at liberty to choose the classification algorithm. Whether it be a decision tree, random forest, or a more sophisticated ensemble or deep learning model, select the one that you believe will perform the best for this classification task.

## 📂 Repository Structure

The repository structure is provided and must be adhered to strictly:

```
|__README.md
|__requirements.txt
|
|__data
|  |__train.csv
|  |__test.csv
|
|__src
|  |__data_processing.py
|  |__model_training.py 
|  |__model_prediction.py
|  |__utils.py
|
|__models
|  |__model.pkl
|
|__scripts
|  |__run_pipeline.sh
|
|__predictions
   |__example_predictions.json
   |__predictions.json


```


You should complete the scripts in the `src` folder, particularly those for model training and prediction. The `data` folder contains the `train.csv` and `test.csv` files, which you will use to train your model and to predict insect categories, respectively.

The `models` folder will store the trained classification model, and the `predictions` folder should contain the `predictions.json` file with your model's predicted insect categories.


## 🎯 Tasks

- Task 1: Your mission is to <u>**develop a model that classifies insects into their respective categories (0, 1, or 2) based on sensor readings**</u>. This task will involve analyzing sensor data like 'Sensor_alpha', 'Sensor_beta', and so on, to determine the category of the insect. The provided dataset has been pre-processed and split into training and testing sets for your convenience.


## 📤 Submission

To carry out this challenge, we expect to obtain a file in json format whose name is `predictions.json`, where we will have as key the `Unnamed : 0` column from the `test.csv`and as value the prediction of the `Insect`, that has as values 0, 1 and 2.
`predictions.json`:
```json
{
    "target": {
        "1": 0,
        "2": 3,
        "3": 8,
        "4": 5,
        "5": 2,
        "6": 7,
        "7": 4,
        "8": 1,
        "9": 6,
        "10": 3,
        ...
    }
}
```

## 📊 Evaluation

As part of our commitment to providing a clear and consistent assessment of performance, this practice will be evaluated automatically using the **F1 Score** metric. The evaluation process will utilize the `predictions.json` file, which contains your model's predictions.

The F1 Score is a widely recognized statistical measure used in classification tests, which balances precision and recall. It is especially useful in scenarios where the distribution of class labels is uneven, providing a more nuanced insight into the model's predictive power.

Your predictions will be compared against the true values to calculate the precision and recall, from which the F1 Score will be derived. The closer your F1 Score is to 1, the better your model's performance is considered in terms of both accuracy and robustness.

Please ensure that your `predictions.json` file follows the correct format as specified in the practice guidelines. The automatic evaluation system will parse this file and apply the F1 Score calculation to determine your final assessment.

- Task 1: 900 points

**⚠️ Please note:**  
All submissions might undergo a manual code review process to ensure that the work has been conducted honestly and adheres to the highest standards of academic integrity. Any form of dishonesty or misconduct will be addressed seriously, and may lead to disqualification from the challenge.

## ❓ FAQs

**Q1: What is the goal of the Insect Classification Hackathon?**  
A1: The goal is to develop a model that classifies insects into their respective categories using sensor environmental data. The model will aid in informing insect conservation strategies and maintaining ecosystem balance.

**Q2: What classification algorithms are recommended for this challenge?**  
A2: You are free to choose any classification algorithm you prefer. Options include decision trees, random forests, or more sophisticated models like ensembles or deep learning. Select the one you believe will perform best for the classification task.

**Q3: What should the `predictions.json` file I submit contain?**  
A3: The `predictions.json` file should contain your model's predictions in JSON format, where the key is the `Unnamed: 0` and the value is the insect category prediction. The format should follow the schema provided in the challenge guidelines.

**Q4: How will my model's predictions be evaluated in this challenge?**  
A4: Predictions will be automatically evaluated using the F1 Score metric, which balances precision and recall. This is particularly useful in scenarios where class label distribution is uneven, providing a more nuanced insight into the model's predictive power.

**Q5: How to solve the challenge?**
A5:
- Solve the proposed objectives.
- Make a push with the changes made.
- Click on Submit Challenge.
- Wait for the results.