import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler

def load_data(train_path, test_path):
    """Carga los archivos CSV en DataFrames de Pandas."""
    train_df = pd.read_csv(train_path)
    test_df = pd.read_csv(test_path)
    return train_df, test_df

def preprocess_data(df, is_train=True):
    """Aplica limpieza y transformación a los datos."""
    # Eliminar columnas innecesarias (por si hay un índice duplicado)
    if 'Unnamed: 0' in df.columns:
        df = df.set_index('Unnamed: 0')

    # Transformar las variables de tiempo en sin/cos para modelos ML
    df["hour_sin"] = np.sin(2 * np.pi * df["Hour"] / 24)
    df["hour_cos"] = np.cos(2 * np.pi * df["Hour"] / 24)
    df.drop(["Hour", "Minutes"], axis=1, inplace=True)  # Ya no necesitamos Hour y Minutes

    # Separar características y target
    if is_train:
        X = df.drop(columns=["Insect"])
        y = df["Insect"]
        return X, y
    else:
        return df  # Solo devolvemos X en test

def scale_data(X_train, X_test):
    """Escala los datos con StandardScaler."""
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)
    return X_train_scaled, X_test_scaled

if __name__ == "__main__":
    train_df, test_df = load_data("../data/train.csv", "../data/test.csv")
    X_train, y_train = preprocess_data(train_df, is_train=True)
    X_test = preprocess_data(test_df, is_train=False)
    X_train_scaled, X_test_scaled = scale_data(X_train, X_test)
