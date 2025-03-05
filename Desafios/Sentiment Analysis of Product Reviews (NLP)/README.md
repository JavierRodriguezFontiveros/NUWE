# 🧠 Sentiment Analysis of Product Reviews 🌟

Category   ➡️   Data Science

Subcategory   ➡️   NLP Engineer

Difficulty   ➡️   Medium

Expected solution time ➡️ 4 hours. **It is essential to complete your solution within this timeframe,** as it is a critical performance indicator used by the hiring company to evaluate your work. The timer will begin when you click the start button and will stop upon your submission.

---

## 🌐 Background

In the age of the Internet, the e-commerce market has seen exponential growth. A pivotal part of online shopping is the reviews that users drop on products they've purchased. These reviews, a treasure trove of data, when analyzed right, can deliver deep insights into customer satisfaction and market preferences.

A leading e-commerce company wishes to harness this data goldmine. They've amassed a plethora of product reviews and are gearing up to analyze them to enhance the service quality they deliver. This is where YOUR challenge begins.

![Image](https://cdn.nuwe.io/infojobs-data/__images/NLP1_SentimentAnalysis.jpeg)


### 🗂️ Dataset 

The dataset you'll be harnessing is a modified version of an Amazon product reviews set, consisting of:

- `Summary`: A succinct review summary penned by the user.
- `Text`: The full-blown review content.

The `Score` column, representing the product rating given by the user (on a 1 to 5 scale), has been axed for this challenge. This will be the target variable your model must predict.


## 📂 Repository Structure

The repository structure is provided and must be adhered to strictly:

```
nuwe-data-nlp1/
├── predictions
│   └── example_predictions.json
├── README.md
├── test
│   └── test.csv
└── train
    └── train.csv
```

## 🎯 Tasks

- Task 1: Your mission, should you choose to accept it, is to craft a classification model that can predict the "rating" of a review, relying solely on its textual content. The company has made some tweaks and omissions to safeguard user privacy. Thus, they'll supply you with just two features to work on: 'Summary' and 'Text'.


## 📤 Submission

To carry out this challenge, we expect to obtain a file in json format whose name is `predictions.json`, where we will have as key the `Test_id`, from the file test/test.csv and as value the prediction of the `Score` column that has values ranging from 1 to 5.
predictions.json:
```json
{
    "target": {
        "297": 1,
        "11": 5,
        "67": 1,
        "54": 3,
        "156": 2,
        "290": 2,
        "193": 4,
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
All submissions will undergo a manual code review process to ensure that the work has been conducted honestly and adheres to the highest standards of academic integrity. Any form of dishonesty or misconduct will be addressed seriously, and may lead to disqualification from the challenge.
The file to be evaluated will be **predictions.json**. This file must be inside **/predictions**.

## ❓ FAQs

**Q1: What is the aim of analyzing product reviews in this challenge?**  
A1: The aim is to develop a classification model that can predict the "rating" of a review based solely on its textual content, providing the e-commerce company with deeper insights into customer satisfaction and market preferences.

**Q2: What information is provided in the dataset for developing the sentiment analysis model?**  
A2: The dataset provides two features: 'Summary', which is a concise review summary, and 'Text', which is the full content of the review.

**Q3: What does the `Score` column represent that the model needs to predict?**  
A3: The `Score` column represents the product rating given by the user on a scale of 1 to 5, and it is the target variable that your model needs to predict.

**Q4: How will the model's predictions be evaluated in this challenge?**  
A4: Predictions will be automatically evaluated using the F1 Score metric, which balances precision and recall, offering a more detailed view of the model's predictive power in text classification.
