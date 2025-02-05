import pandas as pd
import argparse
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier  # Puedes elegir otro modelo si lo prefieres
from sklearn.preprocessing import StandardScaler
import joblib  # Para guardar el modelo

def load_data(file_path):
    """
    Cargar los datos procesados desde un archivo CSV.
    """
    df = pd.read_csv(file_path)
    return df

def split_data(df):
    """
    Dividir los datos en conjuntos de entrenamiento y validación.
    """
    # Asumimos que el DataFrame ya tiene las características y etiquetas preprocesadas
    X = df.drop(columns=['Unnamed: 0', 'Insect'])  # Eliminamos 'Unnamed: 0' y 'Insect' (etiquetas)
    y = df['Insect']  # La etiqueta que queremos predecir
    
    # Dividimos en entrenamiento y validación (80% entrenamiento, 20% validación)
    X_train, X_val, y_train, y_val = train_test_split(X, y, test_size=0.2, random_state=42)
    
    return X_train, X_val, y_train, y_val

def train_model(X_train, y_train):
    """
    Inicializar y entrenar el modelo.
    """
    # Usamos un RandomForestClassifier, pero puedes probar otros modelos
    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)
    
    return model


def save_model(model, model_path):
    """
    Guardar el modelo entrenado en un archivo pickle.
    """
    joblib.dump(model, model_path)
    print(f"Modelo guardado en: {model_path}")

def parse_arguments():
    """
    Manejar los argumentos desde la terminal.
    """
    parser = argparse.ArgumentParser(description='Model training script for Insect Classification Hackathon')
    parser.add_argument(
        '--input_file', 
        type=str, 
        default='../src/data/processed_data.csv',  # Ajustada la ruta a relativa
        help='Ruta al archivo de datos procesados para entrenar el modelo'
    )
    parser.add_argument(
        '--model_file', 
        type=str, 
        default='../models/model.pkl',  # Ajustada la ruta a relativa
        help='Ruta para guardar el modelo entrenado'
    )
    return parser.parse_args()

def main(input_file, model_file):
    """
    Función principal que coordina el flujo de entrenamiento.
    """
    df = load_data(input_file)
    X_train, X_val, y_train, y_val = split_data(df)
    model = train_model(X_train, y_train)
    save_model(model, model_file)

if __name__ == "__main__":
    args = parse_arguments()
    main(args.input_file, args.model_file)
