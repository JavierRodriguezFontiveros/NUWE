#!/bin/bash

# You can run this script from the command line using:
# ./run_pipeline.sh  <raw_data_file> <processed_data_file> <model_file> <test_data_file> <predictions_file>
# For example:
# ./run_pipeline.sh  data/train.csv data/processed_data.csv models/model.pkl data/test.csv predictions/predictions.json

# Get command line arguments
raw_data_file="$1"
processed_data_file="$2"
model_file="$3"
test_data_file="$4"
predictions_file="$5"

# Run data_processing.py
echo "Starting data processing..."
python src/data_processing.py --input_file="$raw_data_file" --output_file="$processed_data_file"

# Run model_training.py
echo "Starting model training..."
python src/model_training.py --input_file="$processed_data_file" --model_file="$model_file"

# Run model_prediction.py
echo "Starting prediction..."
python src/model_prediction.py --input_file="$test_data_file" --model_file="$model_file" --output_file="$predictions_file"

echo "Pipeline completed."
