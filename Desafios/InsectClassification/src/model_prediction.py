import pandas as pd
import argparse
import joblib  # Usamos joblib para cargar el modelo guardado

def load_data(file_path):
    """
    Cargar los datos de prueba desde un archivo CSV.
    """
    df = pd.read_csv(file_path)
    return df

def load_model(model_path):
    """
    Cargar el modelo entrenado desde un archivo pickle (.pkl).
    """
    model = joblib.load(model_path)
    return model

def make_predictions(df, model):
    """
    Realizar predicciones sobre los datos de prueba utilizando el modelo.
    """
    X_test = df.drop(columns=['Unnamed: 0'])  # Eliminar la columna 'Unnamed: 0', ya que no es relevante para el modelo
    predictions = model.predict(X_test)  # Realizamos las predicciones con el modelo
    return predictions

def save_predictions(predictions, predictions_file, df):
    """
    Guardar las predicciones en un archivo JSON en el formato requerido.
    """
    result = {"target": {}}
    for idx, pred in enumerate(predictions):
        result["target"][str(int(df['Unnamed: 0'][idx]))] = int(pred)  # Convertimos la predicción a int
    
    # Guardar el diccionario como un archivo JSON
    with open(predictions_file, 'w') as f:
        import json
        json.dump(result, f, indent=4)
    
    print(f"Predicciones guardadas en: {predictions_file}")

def parse_arguments():
    """
    Manejar los argumentos desde la terminal.
    """
    parser = argparse.ArgumentParser(description='Prediction script for Energy Forecasting Hackathon')
    parser.add_argument(
        '--input_file', 
        type=str, 
        default='../data/processed_test_data.csv',  # Ruta actualizada a los datos procesados
        help='Ruta al archivo de datos de prueba para hacer las predicciones'
    )
    parser.add_argument(
        '--model_file', 
        type=str, 
        default='../models/model.pkl',  # Ruta del modelo entrenado
        help='Ruta al archivo del modelo entrenado'
    )
    parser.add_argument(
        '--output_file', 
        type=str, 
        default='../predictions/predictions.json',  # Ruta para guardar las predicciones
        help='Ruta para guardar las predicciones en formato JSON'
    )
    return parser.parse_args()

def main(input_file, model_file, output_file):
    """
    Función principal que coordina el flujo de predicción.
    """
    df = load_data(input_file)
    model = load_model(model_file)
    predictions = make_predictions(df, model)
    save_predictions(predictions, output_file, df)

if __name__ == "__main__":
    args = parse_arguments()
    main(args.input_file, args.model_file, args.output_file)
