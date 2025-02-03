# 📈 Smart Strategies for an Effective Marketing Campaign 🚀

Category   ➡️   Data Science

Subcategory   ➡️   Data Scientist

Difficulty   ➡️   Easy

Expected solution time ➡️ 4 hours. **It is essential to complete your solution within this timeframe,** as it is a critical performance indicator used by the hiring company to evaluate your work. The timer will begin when you click the start button and will stop upon your submission.

## 🌐 Background

Welcome to the digital heart of the modern era, where information flows boundlessly, and businesses continuously seek innovative ways to capture customer attention. "ClickStream," a budding enterprise in the competitive e-commerce space, grapples with the challenge of understanding its users to serve them better. Recognizing that an engaged customer is the linchpin of growth, ClickStream is keen to deploy smarter marketing strategies.

Until now, ClickStream has leaned on one-size-fits-all marketing tactics. But in a world where every click is gold, personalization is paramount. The firm has amassed a plethora of data regarding user interactions with their platform. Yet, without the application of data science, these digits remain just that – numbers.

Your role as a data scientist is pivotal. By deciphering user behavior patterns, you can assist ClickStream in transmuting this data into bespoke marketing stratagems. In doing so, you're not only bolstering sales but also enhancing the customer journey.

![Image](https://cdn.nuwe.io/infojobs-data/__images/DS1_ClientSegmentation.jpeg)


### 🗂️ Dataset 

You'll be equipped with two detailed datasets, offering a deep dive into user interactions with the ClickStream platform.

1. `user_data.csv` - User Data

This set embodies user demographic and behavior details. Each row corresponds to a unique user.

- `user_id`: The unique identifier for the user.
- `age`: The user's age.
- `abandoned_cart`: Whether the user has left a shopping cart without making a purchase (True/False).
- `user_category`: User category based on their history (new_user, recurring_user, premium_subscriber).
  
2. `session_data.csv` - Session Data

This table delineates specifics about individual user browsing sessions. Each row represents a distinct session.

- `user_id`: The unique user identifier for the user.
- `session_id`: The unique session identifier.
- `timestamp`: The date and time the session commenced.
- `device_type`: Device type used (mobile, tablet, desktop).
- `browser`: The web browser employed during the session.
- `operating_system`: The device's operating system.
- `ip_address`: The user device's IP address.
- `country`: The country from which the session originated.
- `search_query`: Search terms the user inputted on the website.
- `page_views`: Number of pages viewed during the session.
- `session_duration`: The session's total duration in seconds.

### 📊 Data Processing

uring data processing, missing values should be handled, ensuring the dataset is complete and suitable for analysis. Participants are encouraged to user feature engineering techniques  to derive new features and transform existing ones, enhancing the model's predictive power. 

### 🤖 Model

You are at liberty to choose the classification algorithm. Whether it be a decision tree, random forest, or a more sophisticated ensemble or deep learning model, select the one that you believe will perform the best for this classification task.

## 📂 Repository Structure

The repository structure is provided and must be adhered to strictly:

```
nuwe-data-ds1/
├── model.py
├── predictions
│   └── example_predictions.json
├── README.md
├── requirements.txt
├── test
│   ├── session_test.csv
│   └── user_test.csv
└── train
    ├── session_train.csv
    └── user_train.csv


```
- `test/`: Folder containing the user and session data for model validation.
- `train/`: Folder containing the user and session data for model training

## 🎯 Tasks

You're tasked with concocting a predictive model that, harnessing user behavioral data, can pinpoint patterns and guide the marketing team in user segmentation for future campaigns. This segmentation will empower ClickStream to channel its resources more adroitly and craft messages that resonate with diverse user types.

As part of your responsibilities for the ClickStream challenge, you are expected to perform the following key tasks:

- Task 1: Calculate the `marketing_target` Column. Utilize the provided datasets to compute and assign a `marketing_target` value for each user. This column should reflect the predicted engagement level of the user with the marketing campaign, with values assigned as follows:
  - `1` for Low engagement
  - `2` for Medium engagement
  - `3` for High engagement

Your calculated `marketing_target` values will be crucial for the subsequent segmentation and personalization of the marketing strategies. Ensure your predictions are as accurate as possible to facilitate effective user engagement.


## 📤 Submission

To carry out this challenge, we expect to obtain a file in json format whose name is `predictions.json`, where we will have as key the test_id, from the file user_test.csv and as value the prediction of the `marketing_target` column that has as values 1,2 and 3 (low, medium, high).
predictions.json:
```json
{
    "target": {
        "297": 1,
        "11": 3,
        "67": 3,
        "54": 3,
        "156": 2,
        "290": 2,
        "193": 3,
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
The file to be evaluated will be **predictions.json**. This file must be inside **/predictions**.

## ❓ FAQs

*Q1: What is the primary objective of the ClickStream marketing campaign challenge?**  
A1: The primary objective is to develop a predictive model that uses user behavioral data to identify patterns and guide the marketing team in segmenting users for more personalized and effective future campaigns.

**Q2: What datasets are provided for the ClickStream challenge, and what kind of information do they contain?**  
A2: Two datasets are provided: `user_data.csv` contains demographic and behavioral details of users, while `session_data.csv` includes specifics about individual user browsing sessions. They contain various attributes such as `user_id`, `age`, `abandoned_cart`, `user_category`, `session_id`, `device_type`, `browser`, `operating_system`, `ip_address`, `country`, `search_query`, `page_views`, and `session_duration`.

**Q3: What format should the submission be in for the ClickStream challenge?**  
A3: The submission should be in the form of a JSON file named `predictions.json`, which will map `test_id` to the predicted `marketing_target` value (1 for low, 2 for medium, 3 for high).

**Q4: How will the submissions be evaluated in the ClickStream challenge?**  
A4: Submissions will be automatically evaluated using the F1 Score metric, which considers both precision and recall. The F1 Score provides insights into the model's accuracy and ability to generalize on unseen data.

**Q5: Why is the F1 Score used for evaluation in this challenge?**  
A5: The F1 Score is used because it is a harmonic mean of precision and recall, offering a balance between the two, which is particularly beneficial in situations with uneven class distribution. It provides a more comprehensive measure of a model's performance.

**Q6: What are the key considerations for the predictive model to be developed in this challenge?**  
A6: The model should not only be precise but also interpretable and capable of generalizing well on unseen data to ensure it can be effectively used for future marketing campaigns.

**Q7: In the context of the ClickStream challenge, what does the `marketing_target` column represent?**  
A7: The `marketing_target` column represents the classification of the marketing engagement level of a user, with 1 indicating low, 2 indicating medium, and 3 indicating high engagement.

**Q8: What are the expected values for the `marketing_target` prediction, and what do they signify?**  
A8: The expected values for the `marketing_target` prediction are 1, 2, and 3, signifying low, medium, and high potential engagement levels with the marketing campaign, respectively.