import pandas as pd
import json
import pickle
import os
from sklearn.model_selection import TimeSeriesSplit
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import r2_score

# Cargar los datos
data = pd.read_csv("data/raw/measurement_data.csv")

# Convertir la columna de fecha a tipo datetime
data['Measurement date'] = pd.to_datetime(data['Measurement date'])

# Extraer características temporales
data["hour"] = data["Measurement date"].dt.hour
data["month"] = data["Measurement date"].dt.month
data["weekday"] = data["Measurement date"].dt.weekday
data["is_weekend"] = data["weekday"].isin([5, 6]).astype(int)



# Definir las estaciones y los contaminantes
stations = {
    "206": ("SO2", '2023-07-01 00:00:00', '2023-07-31 23:00:00'),
    "211": ("NO2", '2023-08-01 00:00:00', '2023-08-31 23:00:00'),
    "217": ("O3", '2023-09-01 00:00:00', '2023-09-30 23:00:00'),
    "219": ("CO", '2023-10-01 00:00:00', '2023-10-31 23:00:00'),
    "225": ("PM10", '2023-11-01 00:00:00', '2023-11-30 23:00:00'),
    "228": ("PM2.5", '2023-12-01 00:00:00', '2023-12-31 23:00:00')
}

# Inicializar el diccionario de resultados
output = {"target": {}}

# Iterar sobre cada estación y contaminante
for station_code, (pollutant, start_date, end_date) in stations.items():
    # Filtrar los datos para la estación y contaminante
    station_data = data[(data['Station code'] == int(station_code)) & (data[pollutant] >= 0)].copy()
    
    if station_data.empty:
        print(f"No hay datos suficientes para la estación {station_code} y contaminante {pollutant}")
        continue

    # Extraer características temporales
    station_data['day'] = station_data['Measurement date'].dt.day

    # Definir X (características) y y (target)
    X = station_data[['hour', 'day', 'weekday', 'month']]
    y = station_data[pollutant]

    # División de datos en series temporales
    tscv = TimeSeriesSplit(n_splits=5)
    for train_index, test_index in tscv.split(X):
        X_train, X_test = X.iloc[train_index], X.iloc[test_index]
        y_train, y_test = y.iloc[train_index], y.iloc[test_index]

    # Crear el modelo
    model = RandomForestRegressor(max_depth=5, n_estimators=100, random_state=42)

    # Entrenar el modelo
    model.fit(X_train, y_train)

    # Hacer predicciones
    y_pred = model.predict(X_test)

    # Calcular R2
    r2 = r2_score(y_test, y_pred)
    print(f'R² para estación {station_code} y contaminante {pollutant}: {r2:.4f}')

    # Predicción para el período solicitado
    period_start = pd.to_datetime(start_date)
    period_end = pd.to_datetime(end_date)
    date_range = pd.date_range(start=period_start, end=period_end, freq='H')

    # Crear DataFrame con características temporales para predicción
    forecast_data = pd.DataFrame({'Measurement date': date_range})
    forecast_data['hour'] = forecast_data['Measurement date'].dt.hour
    forecast_data['day'] = forecast_data['Measurement date'].dt.day
    forecast_data['weekday'] = forecast_data['Measurement date'].dt.weekday
    forecast_data['month'] = forecast_data['Measurement date'].dt.month

    # Generar predicciones
    predictions = model.predict(forecast_data[['hour', 'day', 'weekday', 'month']])

    # Guardar las predicciones en el diccionario de salida
    output["target"][station_code] = dict(zip(forecast_data['Measurement date'].astype(str), predictions))

    # Guardar el modelo entrenado
    model_filename = f"models/model_task_2_station_{station_code}.pkl"
    os.makedirs(os.path.dirname(model_filename), exist_ok=True)
    with open(model_filename, 'wb') as f:
        pickle.dump(model, f)

# Guardar las predicciones en un archivo JSON
output_filename = "predictions/predictions_task_2.json"
os.makedirs(os.path.dirname(output_filename), exist_ok=True)
with open(output_filename, 'w') as f:
    json.dump(output, f, indent=4)

# Imprimir el resultado
print(f"Predicciones guardadas en {output_filename}")

# Comparar predicciones y valores reales (si tienes los datos reales)
import matplotlib.pyplot as plt
plt.plot(y_test.index, y_test, label='Valores reales')
plt.plot(y_test.index, y_pred, label='Predicciones')
plt.legend()
plt.show()