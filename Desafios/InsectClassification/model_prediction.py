import pickle
import json
import pandas as pd
from data_processing import load_data, preprocess_data, scale_data

def load_model(model_path="../models/model.pkl"):
    """Carga el modelo guardado."""
    with open(model_path, "rb") as f:
        model = pickle.load(f)
    return model

def predict(model, X_test, test_df):
    """Genera predicciones en test.csv y devuelve un diccionario."""
    predictions = model.predict(X_test)
    
    # Crear estructura de salida en JSON
    pred_dict = {"target": dict(zip(test_df.index.astype(str), predictions))}
    
    return pred_dict

def save_predictions(pred_dict, output_path="../predictions/predictions.json"):
    """Guarda las predicciones en formato JSON."""
    with open(output_path, "w") as f:
        json.dump(pred_dict, f, indent=4)

if __name__ == "__main__":
    _, test_df = load_data("../data/train.csv", "../data/test.csv")
    X_test = preprocess_data(test_df, is_train=False)
    _, X_test_scaled = scale_data(X_test, X_test)  # Solo escalar test

    model = load_model()
    predictions = predict(model, X_test_scaled, test_df)
    save_predictions(predictions)
