import pickle
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import f1_score
from data_processing import load_data, preprocess_data, scale_data

def train_model(X, y):
    """Entrena un modelo RandomForest y devuelve el mejor modelo entrenado."""
    X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, stratify=y, random_state=42)
    
    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)

    # Evaluación con F1 Score
    y_pred = model.predict(X_val)
    f1 = f1_score(y_val, y_pred, average="macro")
    print(f"F1 Score en validación: {f1:.4f}")

    return model

def save_model(model, model_path="../models/model.pkl"):
    """Guarda el modelo entrenado en un archivo .pkl"""
    with open(model_path, "wb") as f:
        pickle.dump(model, f)

if __name__ == "__main__":
    train_df, _ = load_data("../data/train.csv", "../data/test.csv")
    X_train, y_train = preprocess_data(train_df, is_train=True)
    X_train_scaled, _ = scale_data(X_train, X_train)  # Escalado solo para entrenamiento

    model = train_model(X_train_scaled, y_train)
    save_model(model)
