
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score

import pandas as pd
import numpy as np
import json

measurement_df = pd.read_csv("../../data/raw/measurement_data.csv")
measurement_df['Measurement date'] = pd.to_datetime(measurement_df['Measurement date'])
measurement_df['Year'] = measurement_df['Measurement date'].dt.year
measurement_df['Month'] = measurement_df['Measurement date'].dt.month
measurement_df['Day'] = measurement_df['Measurement date'].dt.day
measurement_df['Hour'] = measurement_df['Measurement date'].dt.hour

instrument_df = pd.read_csv("../../data/raw/instrument_data.csv")
instrument_df['Measurement date'] = pd.to_datetime(instrument_df['Measurement date'])

pollutant_df = pd.read_csv("../../data/raw/pollutant_data.csv")

item_def_df = pd.merge(instrument_df, pollutant_df, on=["Item code"], how="inner")

combined_df = pd.merge(measurement_df,item_def_df, on=["Measurement date", "Station code"], how="inner")

combined_df["Instrument status label"] = np.where(combined_df["Instrument status"] == 0, "Normal", np.where(combined_df["Instrument status"] == 1, "Necesita calibración", np.where(combined_df["Instrument status"] == 2, "Anormal", np.where(combined_df["Instrument status"] == 4, "Corte de energia", np.where(combined_df["Instrument status"] == 8, "En reparación", np.where(combined_df["Instrument status"] == 9, "Datos anómalos", "No existe código de estado"))))))

items = combined_df["Item name"].unique()
columns = combined_df.columns


for item in items:
    for column in columns:
        if column in items:
            combined_df.loc[combined_df["Item name"] != column, column] = np.nan

def classify_air_quality(row):
    value = row[items].max()  # Tomamos el valor máximo de las columnas que contienen mediciones
    
    if value <= row["Good"]:
        return "Good"
    elif value <= row["Normal"]:
        return "Normal"
    elif value <= row["Bad"]:
        return "Bad"
    elif value <= row["Very bad"]:
        return "Very bad"
    else:
        return np.nan  # En caso de que no se pueda clasificar
    

# Aplicamos la función fila por fila
combined_df["Air quality"] = combined_df.apply(classify_air_quality, axis=1)      

combined_df = pd.read_csv("../../data/processed/combined_data.csv", index_col=0)

combined_df['Measurement date'] = pd.to_datetime(combined_df['Measurement date'])

normal_inst_state_df = combined_df[combined_df["Instrument status label"] == "Normal"].reset_index()

normal_inst_state_df = normal_inst_state_df.drop(columns=["index"])

#Estación206:

so2_206_julio = normal_inst_state_df[normal_inst_state_df["SO2"].notna()] 
#so2_206_julio = so2_206_julio[so2_206_julio["SO2"].notna()]
#so2_206_julio = so2_206_julio[so2_206_julio["Month"] == 7]
#so2_206_julio = so2_206_julio[so2_206_julio["Year"] == 2023]
so2_206_julio = so2_206_julio.dropna(axis=1)
so2_206_julio

so2_206_julio['day_of_week'] = so2_206_julio['Measurement date'].dt.dayofweek

X = so2_206_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = so2_206_julio['SO2']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Crear el modelo de Random Forest
rf_model = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model.fit(X_train, y_train)

# Predicciones con los datos de prueba
y_pred = rf_model.predict(X_test)

# Evaluar el rendimiento del modelo
r2s = r2_score(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'R2 score: {r2s}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')

periodo = pd.date_range(start='2023-07-01 00:00:00', end='2023-07-31 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo = pd.DataFrame(periodo, columns=['timestamp'])
df_periodo['day_of_week'] = df_periodo['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo["Hour"] = df_periodo['timestamp'].dt.hour  # Hora del día
df_periodo["Month"] = df_periodo['timestamp'].dt.month  # Mes
df_periodo["Day"] = df_periodo['timestamp'].dt.day  # Día del mes
df_periodo["Year"] = df_periodo['timestamp'].dt.year  # Día del mes

x_pred = df_periodo[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred["Station code"] = 206

y_pred_periodo = rf_model.predict(x_pred)

df_predicciones_stat_206 = df_periodo.copy()
df_predicciones_stat_206['predicted_SO2'] = y_pred_periodo

df_predicciones_stat_206['date_str'] = df_predicciones_stat_206['timestamp'].dt.strftime('%Y-%m-%d %H:%M:%S')
sol_206 = dict(zip(df_predicciones_stat_206['date_str'], df_predicciones_stat_206["predicted_SO2"]))

#Estación 211:

no2_211_julio = normal_inst_state_df[normal_inst_state_df["NO2"].notna()] 
no2_211_julio = no2_211_julio.dropna(axis=1)

no2_211_julio['day_of_week'] = no2_211_julio['Measurement date'].dt.dayofweek

X = no2_211_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = no2_211_julio['NO2']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Crear el modelo de Random Forest
rf_model211 = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model211.fit(X_train, y_train)

# Predicciones con los datos de prueba
y_pred = rf_model211.predict(X_test)

# Evaluar el rendimiento del modelo
mse = mean_squared_error(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'Mean Squared Error: {mse}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')

periodo211 = pd.date_range(start='2023-08-01 00:00:00', end='2023-08-31 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo211 = pd.DataFrame(periodo211, columns=['timestamp'])
df_periodo211['day_of_week'] = df_periodo211['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo211["Hour"] = df_periodo211['timestamp'].dt.hour  # Hora del día
df_periodo211["Month"] = df_periodo211['timestamp'].dt.month  # Mes
df_periodo211["Day"] = df_periodo211['timestamp'].dt.day  # Día del mes
df_periodo211["Year"] = df_periodo211['timestamp'].dt.year  # Día del mes

x_pred211 = df_periodo211[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred211["Station code"] = 211

y_pred_periodo211 = rf_model211.predict(x_pred211)

df_predicciones_stat_211 = df_periodo211.copy()
df_predicciones_stat_211['predicted_NO2'] = y_pred_periodo211

sol_211 = dict(zip(df_predicciones_stat_211['date_str'], df_predicciones_stat_211["predicted_NO2"]))


#Estación 217:

o3_217_julio = normal_inst_state_df[normal_inst_state_df["O3"].notna()] 
o3_217_julio = o3_217_julio.dropna(axis=1)
o3_217_julio
o3_217_julio['day_of_week'] = o3_217_julio['Measurement date'].dt.dayofweek


X = o3_217_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = o3_217_julio['O3']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# Crear el modelo de Random Forest
rf_model217 = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model217.fit(X_train, y_train)
# Predicciones con los datos de prueba
y_pred = rf_model217.predict(X_test)

# Evaluar el rendimiento del modelo
mse = mean_squared_error(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'Mean Squared Error: {mse}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')

periodo217 = pd.date_range(start='2023-09-01 00:00:00', end='2023-09-30 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo217 = pd.DataFrame(periodo217, columns=['timestamp'])
df_periodo217['day_of_week'] = df_periodo217['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo217["Hour"] = df_periodo217['timestamp'].dt.hour  # Hora del día
df_periodo217["Month"] = df_periodo217['timestamp'].dt.month  # Mes
df_periodo217["Day"] = df_periodo217['timestamp'].dt.day  # Día del mes
df_periodo217["Year"] = df_periodo217['timestamp'].dt.year  # Día del mes

x_pred217 = df_periodo217[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred217["Station code"] = 217

y_pred_periodo217 = rf_model217.predict(x_pred217)
df_predicciones_stat_217 = df_periodo217.copy()
df_predicciones_stat_217['predicted_O3'] = y_pred_periodo217
df_predicciones_stat_217
df_predicciones_stat_217['date_str'] = df_predicciones_stat_217['timestamp'].dt.strftime('%Y-%m-%d %H:%M:%S')
sol_217 = dict(zip(df_predicciones_stat_217['date_str'], df_predicciones_stat_217["predicted_O3"]))


#Estación 219

co_219_julio = normal_inst_state_df[normal_inst_state_df["CO"].notna()] 
co_219_julio = co_219_julio.dropna(axis=1)
co_219_julio
co_219_julio['day_of_week'] = co_219_julio['Measurement date'].dt.dayofweek


X = co_219_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = co_219_julio['CO']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# Crear el modelo de Random Forest
rf_model219 = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model219.fit(X_train, y_train)
# Predicciones con los datos de prueba
y_pred = rf_model219.predict(X_test)

# Evaluar el rendimiento del modelo
mse = mean_squared_error(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'Mean Squared Error: {mse}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')


periodo219 = pd.date_range(start='2023-10-01 00:00:00', end='2023-10-31 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo219 = pd.DataFrame(periodo219, columns=['timestamp'])
df_periodo219['day_of_week'] = df_periodo219['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo219["Hour"] = df_periodo219['timestamp'].dt.hour  # Hora del día
df_periodo219["Month"] = df_periodo219['timestamp'].dt.month  # Mes
df_periodo219["Day"] = df_periodo219['timestamp'].dt.day  # Día del mes
df_periodo219["Year"] = df_periodo219['timestamp'].dt.year  # Día del mes

x_pred219 = df_periodo219[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred219["Station code"] = 219

y_pred_periodo219 = rf_model219.predict(x_pred219)
df_predicciones_stat_219 = df_periodo219.copy()
df_predicciones_stat_219['predicted_CO'] = y_pred_periodo219
df_predicciones_stat_219
df_predicciones_stat_219['date_str'] = df_predicciones_stat_219['timestamp'].dt.strftime('%Y-%m-%d %H:%M:%S')
sol_219 = dict(zip(df_predicciones_stat_219['date_str'], df_predicciones_stat_219["predicted_CO"]))


#Estación225

pm10_225_julio = normal_inst_state_df[normal_inst_state_df["PM10"].notna()] 

pm10_225_julio = pm10_225_julio.dropna(axis=1)
pm10_225_julio
pm10_225_julio['day_of_week'] = pm10_225_julio['Measurement date'].dt.dayofweek


X = pm10_225_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = pm10_225_julio['PM10']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# Crear el modelo de Random Forest
rf_model225 = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model225.fit(X_train, y_train)
# Predicciones con los datos de prueba
y_pred = rf_model225.predict(X_test)

# Evaluar el rendimiento del modelo
mse = mean_squared_error(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'Mean Squared Error: {mse}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')


periodo225 = pd.date_range(start='2023-11-01 00:00:00', end='2023-11-30 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo225 = pd.DataFrame(periodo225, columns=['timestamp'])
df_periodo225['day_of_week'] = df_periodo225['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo225["Hour"] = df_periodo225['timestamp'].dt.hour  # Hora del día
df_periodo225["Month"] = df_periodo225['timestamp'].dt.month  # Mes
df_periodo225["Day"] = df_periodo225['timestamp'].dt.day  # Día del mes
df_periodo225["Year"] = df_periodo225['timestamp'].dt.year  # Día del mes

x_pred225 = df_periodo225[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred225["Station code"] = 225
x_pred225
y_pred_periodo225 = rf_model225.predict(x_pred225)
df_predicciones_stat_225 = df_periodo225.copy()
df_predicciones_stat_225['predicted_PM10'] = y_pred_periodo225
df_predicciones_stat_225
df_predicciones_stat_225['date_str'] = df_predicciones_stat_225['timestamp'].dt.strftime('%Y-%m-%d %H:%M:%S')
sol_225 = dict(zip(df_predicciones_stat_225['date_str'], df_predicciones_stat_225["predicted_PM10"]))

#Estación228

pm2_228_julio = normal_inst_state_df[normal_inst_state_df["PM2.5"].notna()] 

pm2_228_julio = pm2_228_julio.dropna(axis=1)
pm2_228_julio
pm2_228_julio['day_of_week'] = pm2_228_julio['Measurement date'].dt.dayofweek


X = pm2_228_julio[["Year", "Month", "Day", "day_of_week", "Hour", "Station code"]]  # Variables predictoras
y = pm2_228_julio['PM2.5']  # Variable objetivo (concentración de contaminante)

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
# Crear el modelo de Random Forest
rf_model228 = RandomForestRegressor(n_estimators=100, random_state=42)

# Entrenar el modelo con los datos de entrenamiento
rf_model228.fit(X_train, y_train)
# Predicciones con los datos de prueba
y_pred = rf_model228.predict(X_test)

# Evaluar el rendimiento del modelo
mse = mean_squared_error(y_test, y_pred)
rmse = mean_squared_error(y_test, y_pred, squared=False)
mae = mean_absolute_error(y_test, y_pred)
mae = mean_absolute_error(y_test, y_pred)

print(f'Mean Squared Error: {mse}')
print(f'Root Mean Squared Error: {rmse}')
print(f'Mean Absolute Error: {mae}')


periodo228 = pd.date_range(start='2023-12-01 00:00:00', end='2023-12-31 23:00:00', freq='H')

# Crear un DataFrame con la columna de fecha
df_periodo228 = pd.DataFrame(periodo228, columns=['timestamp'])
df_periodo228['day_of_week'] = df_periodo228['timestamp'].dt.dayofweek  # Lunes=0, Domingo=6
df_periodo228["Hour"] = df_periodo228['timestamp'].dt.hour  # Hora del día
df_periodo228["Month"] = df_periodo228['timestamp'].dt.month  # Mes
df_periodo228["Day"] = df_periodo228['timestamp'].dt.day  # Día del mes
df_periodo228["Year"] = df_periodo228['timestamp'].dt.year  # Día del mes

x_pred228 = df_periodo228[["Year", "Month", "Day", "day_of_week", "Hour"]]
x_pred228["Station code"] = 228
x_pred228
y_pred_periodo228 = rf_model228.predict(x_pred228)
df_predicciones_stat_228 = df_periodo228.copy()
df_predicciones_stat_228['predicted_PM2.5'] = y_pred_periodo228
df_predicciones_stat_228
df_predicciones_stat_228['date_str'] = df_predicciones_stat_228['timestamp'].dt.strftime('%Y-%m-%d %H:%M:%S')
sol_228 = dict(zip(df_predicciones_stat_228['date_str'], df_predicciones_stat_228["predicted_PM2.5"]))



target_2 = {"206": sol_206,
            "211": sol_211,
            "217": sol_217,
            "219": sol_219,
            "225": sol_225,
            "228": sol_228
            }

task2_sol = {"target": target_2}

with open('../../predictions/predictions_task_2.json', 'w') as json_file:
    json.dump(task2_sol, json_file, indent=4)



