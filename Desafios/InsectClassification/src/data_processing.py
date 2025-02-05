import argparse
import pandas as pd
import numpy as np
from sklearn.preprocessing import StandardScaler
import os

def load_data(file_path):
    """
    Cargar los datos desde el archivo CSV.
    """
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"El archivo {file_path} no existe.")
    df = pd.read_csv(file_path)
    return df


def preprocess_data(df):
    """
    Preprocesar los datos: crear nuevas características, transformar características existentes.
    """
    # 1. Crear variables cíclicas para 'Hour' y 'Minutes'
    df['hour_sin'] = np.sin(2 * np.pi * df['Hour'] / 24)
    df['hour_cos'] = np.cos(2 * np.pi * df['Hour'] / 24)
    
    df['minute_sin'] = np.sin(2 * np.pi * df['Minutes'] / 60)
    df['minute_cos'] = np.cos(2 * np.pi * df['Minutes'] / 60)
    
    # 2. Escalar los sensores
    sensor_columns = ['Sensor_alpha', 'Sensor_beta', 'Sensor_gamma', 
                      'Sensor_alpha_plus', 'Sensor_beta_plus', 'Sensor_gamma_plus']
    
    # Si tienes un StandardScaler entrenado, puedes usar el mismo escalador que se usó durante el entrenamiento.
    scaler = StandardScaler()
    df[sensor_columns] = scaler.fit_transform(df[sensor_columns])
    
    return df

def save_data(df, output_file):
    """
    Guardar los datos procesados en un archivo CSV.
    """
    # Asegúrate de que la ruta del archivo de salida sea válida y crea los directorios si no existen
    output_dir = os.path.dirname(output_file)
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)  # Crea el directorio si no existe
    
    df.to_csv(output_file, index=False)
    print(f"Datos procesados guardados en: {output_file}")


def parse_arguments():
    """
    Manejar argumentos desde la terminal para definir los archivos de entrada y salida.
    """
    parser = argparse.ArgumentParser(description='Data processing script for Insect Classification Hackathon')
    parser.add_argument(
        '--input_file',
        type=str,
        default='../data/test.csv',  # Ruta al archivo de datos de prueba
        help='Ruta al archivo de datos crudos para procesar'
    )
    parser.add_argument(
        '--output_file', 
        type=str, 
        default='../data/processed_test_data.csv',  # Ruta para guardar los datos procesados
        help='Ruta para guardar los datos procesados'
    )
    return parser.parse_args()

def main(input_file, output_file):
    """
    Función principal que coordina la carga, preprocesamiento y guardado de los datos de prueba.
    """
    df = load_data(input_file)
    df_processed = preprocess_data(df)  # Aplicamos el preprocesamiento a los datos de prueba
    save_data(df_processed, output_file)

if __name__ == "__main__":
    args = parse_arguments()
    main(args.input_file, args.output_file)
