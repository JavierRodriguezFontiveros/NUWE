# Perspectivas sobre la calidad del aire
## Antecendentes

1. **Problema Central:**

- La contaminación energética derivada del uso de combustibles fósiles.
- Su impacto en el cambio climático y la salud ambiental.

2. **Causas principales:**

- Dependencia global del carbón, petróleo y gas natural.
- Emisiones de CO₂ y otros contaminantes por su combustión.
- Aumento de la demanda energética por crecimiento poblacional e industrialización.

3. **Consecuencias:**

- Aceleración del cambio climático.
- Daño a los ecosistemas.
- Perjuicio a la salud de las comunidades.

4. **Necesidad de soluciones:**

- Transición a energías más limpias.
- Mejora en la eficiencia energética.
- Implementación de prácticas sostenibles.

5. **Objetivo final:**

- Reducir la contaminación energética.
- Mitigar el cambio climático.
- Fomentar comunidades saludables y sostenibles a largo plazo.

## Estructura del repositorio:
```
├── data/                      
│   ├── raw/                      # Datos originales
│   │   └── pollutant_data.csv  
│   └── processed/                 # Datos procesados y limpios
│
│── predictions/   
│   ├── questions.json             # Respuestas de la Tarea 1
│   ├── predictions_task_2.json     # Predicciones del Modelo 1
│   └── predictions_task_3.json     # Predicciones del Modelo 2
│ 
│── models/   
│   ├── model_task_2                # Modelo de pronóstico
│   └── model_task_3                # Modelo de estado del instrumento
│
├── src/                        # Código fuente
│   ├── data/                   
│   │   ├── questions.py         # Código para responder preguntas de Tarea 1
│   │   └── ...                  # Scripts de procesamiento de datos
│   │
│   └── models/
│       └── (preparar los datos y crear los modelos)                       
│
└── README.md                     # Documentación del proyecto
```


## DataSets

# 1️⃣ Measurement data
### 📌 Variables y su Interpretación:
1. ✅**Measurement date** (Fecha de medición)
    - Indica el momento en que se registraron los datos.
    - Permite analizar tendencias temporales (diarias, estacionales, anuales).
    - Puede utilizarse para detectar patrones de contaminación o eventos extremos.

2. ✅**Station code** (Código de estación)

    - Identificador único de la estación de monitoreo.
    - Permite asociar mediciones con una ubicación específica.
    - Se puede usar para agrupar datos por estación y comparar áreas.

3. ✅**Latitude**
4. ✅**Longitude**
    - Indican la ubicación geográfica de cada estación.
    - Permiten realizar análisis espaciales y mapas de contaminación.
    - Facilitan la integración con otros datos geoespaciales (ej. población, tráfico, vegetación).

### 📌 Contaminantes Medidos:
5. ✅**SO₂** (Dióxido de azufre)
    - Proviene de la quema de combustibles fósiles y procesos industriales.
    - Causa problemas respiratorios y contribuye a la lluvia ácida.
    - Se puede analizar su concentración en relación con fuentes industriales y meteorología.

6. ✅**NO₂** (Dióxido de nitrógeno)
    - Generado principalmente por el tráfico y la combustión de combustibles.
    - Relacionado con enfermedades respiratorias y contaminación por ozono.
    - Se puede analizar su variación en horas pico de tráfico y correlación con O₃.

7. ✅**O₃** (Ozono troposférico)
    - Se forma por reacciones químicas entre NO₂ y compuestos orgánicos volátiles con la luz solar.
    - Alto impacto en la salud y ecosistemas.
    - Se puede estudiar su relación con la radiación solar y otros contaminantes.

8. ✅**O** (Monóxido de carbono)
    - Generado por combustión incompleta (vehículos, fábricas).
    - Puede causar intoxicaciones en altas concentraciones.
    - Se puede analizar su variación horaria y relación con fuentes urbanas.

9. ✅**PM10** partículas menores a 10 µm (polvo, cenizas, polen).
10. ✅**PM2.5** partículas menores a 2.5 µm (humo, aerosoles tóxicos).
    - PM10 y PM2.5 (Material particulado grueso y fino)
    - Impactan gravemente la salud respiratoria y cardiovascular.
    - Se pueden analizar tendencias estacionales, fuentes de emisión y efectos meteorológicos.

### ¿Qué podemos hacer con estos datos?
1. ***Análisis temporal:***

    - ¿Cuáles son los momentos de mayor contaminación? (horas pico, estaciones del año).
    - ¿Cómo han cambiado los niveles de contaminación con el tiempo?

2. ***Análisis espacial:***

    - ¿Qué áreas tienen peor calidad del aire?
    - ¿Cómo se relaciona la contaminación con factores como tráfico o industrias?

3. ***Correlaciones entre variables:***

    - Relación entre NO₂ y O₃ (formación de ozono).
    - Impacto del clima (temperatura, viento, humedad) en la dispersión de contaminantes.
    - Identificación de patrones y anomalías:
    - Detección de eventos extremos (altos niveles de contaminación).
    - Comparación entre días laborales y fines de semana.

4. ***Visualización de datos:***

    - Mapas de contaminación.
    - Gráficos de tendencias y comparaciones entre estaciones.

# 2️⃣ Instrument Data  
### 📌 Variables y su Interpretación:  

1. ✅ **Measurement date** (Fecha de medición) 
 
2. ✅ **Station code**  

3. ✅ **Item code** (Código del ítem medido)  
   - Indica qué contaminante o parámetro se está midiendo.  
   - Permite diferenciar entre mediciones de SO₂, NO₂, O₃, CO, PM10, PM2.5, etc.  

4. ✅ **Average value** (Valor promedio)  
   - Representa el nivel del contaminante medido en un período determinado.  
   - Se puede utilizar para evaluar la calidad del aire y realizar comparaciones entre estaciones.  

5. ✅ **Instrument status** (Estado del instrumento de medición)  
   - Indica la condición del equipo al momento de la medición.  
   - Puede afectar la validez de los datos y requerir filtrado o corrección.  

   **Códigos de estado:**  
   - `0` → Normal ✅  
   - `1` → Necesita calibración ⚠️  
   - `2` → Anormal ❌  
   - `4` → Corte de energía ⚡  
   - `8` → En reparación 🔧  
   - `9` → Datos anómalos 📉  

### 📌 ¿Qué podemos hacer con estos datos?  

1. ***Validación de datos:***  
   - Identificar mediciones con estado anómalo y filtrarlas o corregirlas.  
   - Detectar estaciones que requieran mantenimiento o calibración.  

2. ***Análisis de calidad del aire:***  
   - Evaluar tendencias en los valores promedio de contaminantes.  
   - Comparar mediciones entre estaciones y períodos de tiempo.  

3. ***Análisis de fallos en estaciones de monitoreo:***  
   - Identificar problemas recurrentes (cortes de energía, reparaciones frecuentes).  
   - Evaluar la confiabilidad de los datos en función del estado del instrumento.  

4. ***Visualización de datos:***  
   - Gráficos de evolución de contaminantes con indicadores de estado del instrumento.  
   - Mapas de calidad del aire considerando únicamente mediciones confiables.  

# 3️⃣ Pollutant Data  
### 📌 Variables y su Interpretación:  

1. ✅ **Item code** (Código del contaminante)  

2. ✅ **Item name** (Nombre del contaminante)  
   - Nombre del contaminante o parámetro medido (SO₂, NO₂, O₃, CO, PM10, PM2.5, etc.).  
   - Permite interpretar los valores medidos en función de la sustancia analizada.  

3. ✅ **Unit of measurement** (Unidad de medida)  
   - Define en qué unidad se expresa cada contaminante (µg/m³, ppm, etc.).  
   - Es crucial para la comparación y conversión de datos entre diferentes fuentes.  

4. ✅ **Good** (Bueno)  
   - Umbral o valor máximo para considerar una calidad del aire aceptable.  
   - Se puede usar para clasificar los datos en categorías de contaminación.  

5. ✅ **Normal** (Normal)  
   - Intervalo en el que la calidad del aire es moderada pero no óptima.  
   - Puede indicar contaminación leve sin efectos severos en la salud.  

6. ✅ **Bad** (Malo)  
   - Nivel en el que la contaminación comienza a ser preocupante.  
   - Puede tener impactos negativos en grupos sensibles (niños, ancianos, asmáticos).  

7. ✅ **Very bad** (Muy malo)  
   - Nivel en el que la contaminación es severa y afecta a toda la población.  
   - Puede estar asociado a alertas ambientales y restricciones.  

### 📌 ¿Qué podemos hacer con estos datos?  

1. ***Clasificación de la calidad del aire:***  
   - Usar los umbrales (`Good`, `Normal`, `Bad`, `Very bad`) para categorizar mediciones.  
   - Generar índices de calidad del aire basados en contaminantes específicos.  

2. ***Análisis de riesgos:***  
   - Identificar qué contaminantes superan los niveles recomendados con más frecuencia.  
   - Evaluar el impacto de la contaminación en diferentes regiones y poblaciones.  

3. ***Comparación entre contaminantes:***  
   - Relacionar contaminantes con fuentes de emisión y condiciones climáticas.  
   - Evaluar qué contaminantes son más problemáticos en diferentes estaciones del año.  

4. ***Visualización de datos:***  
   - Mapas de calidad del aire por categorías (`Bueno`, `Malo`, etc.).  
   - Gráficos de tendencias para analizar cómo varían los contaminantes con el tiempo.  


# 📢 Relación entre Datasets
Los tres datasets juntos permiten una evaluación profunda de la calidad del aire, desde la recolección de datos hasta su interpretación.  
Esto posibilita la toma de decisiones informadas, la detección de tendencias y la implementación de políticas para mejorar la calidad del aire en diferentes regiones.

## 📌 Variables comunes entre los datasets:
1. ✅ **Measurement date:**
    - measurement_data.csv
    - instrument_data.csv.

Permite sincronizar mediciones con el estado de los instrumentos.

2. ✅ **Station code:**
    - measurement_data.csv
    - instrument_data.csv.

Permite identificar en qué estación se tomaron las mediciones.

3. ✅ **Item code:**
    - instrument_data.csv
    - pollutant_data.csv.

Relaciona los valores medidos con su respectivo contaminante y umbrales de calidad.




# Pasos a seguir:
## 1️⃣ Tratamiento de Datos
1. ***Filtrado:***
    - Eliminar mediciones con errores (Instrument status ≠ 0)
    - Descartar valores anómalos

2. ***Normalización:***
    - Ajustar escalas de variables para modelos de ML.
    - Convertir unidades.

3. ***Manejo de valores faltantes:***
    - Imputar valores faltantes con la media/mediana o interpolación.
    - Eliminar registros si la cantidad de valores perdidos es alta. (poco recomendable)


## 2️⃣ Modelos de Aprendizaje Automático
### 🔹 **Modelo 1:** *Pronóstico de calidad del aire*
* 📌 Objetivo: **Predecir la calidad del aire por hora en períodos específicos.**

    - Se asume que no hay errores de medición.
    - Modelos de series temporales (ARIMA, LSTM, Prophet).
    - Modelos de regresión (Random Forest, XGBoost, LightGBM).

### 🔹 **Modelo 2:** *Estado del instrumento.*
* 📌 Objetivo: ***Detectar y clasificar fallas en instrumentos de medición y Predecir el estado del instrumento***

    - Modelos de clasificación (Random Forest, XGBoost, SVM, Redes Neuronales).


# Tareas:

## 1️⃣ Análisis Exploratorio de Datos (EDA)

* 📊 **P1: Concentración media diaria de SO₂**
    - Filtrar datos de SO₂ (pollutant_data.csv).
    - Agrupar por fecha y calcular el promedio diario.
    - Tomar el promedio de todas las estaciones y devolver el resultado con 5 decimales.

* 📊 **P2: Variación de CO según la estación (Estación 209)**
    - Filtrar datos de CO en la estación 209.
    - Agrupar por estación del año y calcular los promedios.
    - Considerar: Invierno (Dic-Feb), Primavera (Mar-May), Verano (Jun-Ago), Otoño (Sep-Nov).
    - Respuesta con 5 decimales.

* 📊 **P3: Hora con mayor variabilidad para O₃**
    - Filtrar datos de O₃.
    - Agrupar por hora del día y calcular la desviación estándar.
    - Devolver la hora con mayor variabilidad.

* 📊 **P4: Código de estación con más datos "Anormales" (Instrument status = 9)**
    - Contar cuántos registros tienen Instrument status = 9 por estación.
    - Devolver el código de la estación con más mediciones anormales.

* 📊 **P5: Código de estación con más mediciones "no normales" (Instrument status ≠ 0)**
    - Contar todos los registros con estados diferentes de Normal (0).
    - Identificar la estación con más casos.

* 📊 **P6: Recuento de registros por categoría de calidad para PM2.5**
- Filtrar datos de PM2.5.
- Contar registros en cada categoría: "Good", "Normal", "Bad", "Very bad".

### 📌 Formato de salida esperado

```
{"target":
  {
    "Q1": 0.11111,
    "Q2": {
        "1": 0.11111,
        "2": 0.11111,
        "3": 0.11111,
        "4": 0.11111
    },
    "Q3": 111,
    "Q4": 111,
    "Q5": 111,
    "Q6": {
        "Normal": 111,
        "Good": 111,
        "Bad": 111,
        "Very bad": 111
    }
  }
}
```

## 2️⃣ Modelo de Pronóstico de Calidad del Aire

### 📌 Datos a predecir:
```
Station Code	Pollutant	Periodo
206	SO₂	2023-07-01 → 2023-07-31
211	NO₂	2023-08-01 → 2023-08-31
217	O₃	2023-09-01 → 2023-09-30
219	CO	2023-10-01 → 2023-10-31
225	PM10	2023-11-01 → 2023-11-30
228	PM2.5	2023-12-01 → 2023-12-31
```
### 📌 Formato de salida esperado:
```
{
  "target":
  {
    "206": 
      {
        "2023-07-01 00:00:00": 0.32,
        "2023-07-01 01:00:00": 0.5,
        "2023-07-01 02:00:00": 0.8,
        "2023-07-01 03:00:00": 0.11,
        "2023-07-01 04:00:00": 0.7,
        ...
      },
    "211":
      {
        ...
      }
  }
}
```

## 3️⃣ Detección de Anomalías en Mediciones

## 📌 Datos a analizar:
```
Station Code	Pollutant	Periodo
205	SO₂	2023-11-01 → 2023-11-30
209	NO₂	2023-09-01 → 2023-09-30
223	O₃	2023-07-01 → 2023-07-31
224	CO	2023-10-01 → 2023-10-31
226	PM10	2023-08-01 → 2023-08-31
227	PM2.5	2023-12-01 → 2023-12-31
```

## 📌 Formato de salida esperado:
```
{
  "target":
  {
    "205": 
    {
      "2023-11-01 00:00:00": 5,
      "2023-11-01 01:00:00": 3,
      "2023-11-01 02:00:00": 6,
      "2023-11-01 03:00:00": 1,
      "2023-11-01 05:00:00": 3,
    ...
    },
    "209":
    {
      ...
    }
  }
}
```
# Puntuación:

### ✅ **Tarea 1:** Responder Preguntas del EDA (300 puntos)
### ✅ **Tarea 2:** Pronóstico de Contaminantes (550 puntos, R² Score)
### ✅ **Tarea 3:** Detección de Anomalías (550 puntos, F1 Score)



# Air Quality Insights

Category   ➡️   Data Science

Subcategory   ➡️   Recommender systems

Difficulty   ➡️   Medium


---

## 🌐 Background

The challenge of energy pollution and climate action arises from the global dependency on fossil fuels, which are the primary contributors to greenhouse gas emissions. The combustion of coal, oil, and natural gas for energy production releases carbon dioxide and other harmful pollutants into the atmosphere, accelerating climate change and damaging ecosystems. As energy demand surges due to population growth and industrial development, the environmental impact intensifies. This challenge necessitates innovative solutions to transition towards cleaner energy sources, enhance energy efficiency, and implement sustainable practices. Tackling energy pollution is vital not only for mitigating climate change but also for fostering healthier communities and ensuring long-term environmental sustainability.

### 🗂️ Dataset 

Three distinct datasets will be provided:

- Measurement data:
  - Variables:
    - `Measurement date`
    - `Station code`
    - `Latitude`
    - `Longitude`
    - `SO2`
    - `NO2`
    - `O3`
    - `CO`
    - `PM10`
    - `PM2.5`
 - Data available at: [Download Measurement data](https://cdn.nuwe.io/challenges-ds-datasets/hackathon-schneider-pollutant/measurement_data.zip)


- Instrument data
  - Variables:
    - `Measurement date`
    - `Station code`
    - `Item code`
    - `Average value`
    - `Instrument status` : Status of the measuring instrument when the sample was taken.
    ```json
    {
        0: "Normal",
        1: "Need for calibration",
        2: "Abnormal",
        4: "Power cut off",
        8: "Under repair",
        9: "Abnormal data",
    }
    ```
  - Data available at: [Download Instrument data](https://cdn.nuwe.io/challenges-ds-datasets/hackathon-schneider-pollutant/instrument_data.zip)

- Pollutant data
 - Variables:
    - `Item code`
    - `Item name`
    - `Unit of measurement`
    - `Good`
    - `Normal`
    - `Bad`
    - `Very bad`

  


### 📊 Data Processing

Perform comprehensive data processing, including filtering, normalization, and handling missing values.

Afterwards, develop two machine learning models:

- **Forecast Model:** Predict hourly air quality for specified periods, assuming no measurement errors.

- **Instrument Status Model:** Detect and classify failures in measurement instruments.

## 📂 Repository Structure
The repository structure is provided and must be adhered to strictly:

```

├── data/                      
│   ├── raw/
│   │   └── pollutant_data.csv  
│   └── processed/                 
│
│── predictions/   
│   ├── questions.json 
│   ├── predictions_task_2.json 
│   └── predictions_task_3.json     
│ 
│── models/   
│   ├── model_task_2
│   └── model_task_3
│
├── src/                       
│   ├── data/                   
│   │   ├── questions.py   
│   │   └── ...
│   │
│   └── models/
│       └── (prepare your data and create the model)                      
│
└── README.md 

```

The `/predictions` folder should contain the tasks outputs for the questions in Task 1 and the predictions for both Task 2 and Task 3.
 

## 🎯 Tasks
This challenge will include three tasks: an initial exploratory data analysis task with questions, followed by two model creation tasks.

#### **Task 1:** Answer the following questions about the given datasets:

**IMPORTANT** Answer the following questions considering only measurements with the value tagged as "Normal" (code 0):

  - **Q1:** Average daily SO2 concentration across all districts over the entire period. Give the station average. Provide the answer with 5 decimals.
  - **Q2:** Analyse how pollution levels vary by season. Return the average levels of CO per season at the station 209. (Take the whole month of December as part of winter, March as spring, and so on.) Provide the answer with 5 decimals.
  - **Q3:** Which hour presents the highest variability (Standard Deviation) for the pollutant O3? Treat all stations as equal. 
  - **Q4:** Which is the station code with more measurements labeled as "Abnormal data"? 
  - **Q5:** Which station code has more "not normal" measurements (!= 0)?
  - **Q6:** Return the count of `Good`, `Normal`, `Bad` and `Very bad` records for all the station codes of PM2.5 pollutant.
 

Example question output format:
```json
{"target":
  {
    "Q1": 0.11111,
    "Q2": {
        "1": 0.11111,
        "2": 0.11111,
        "3": 0.11111,
        "4": 0.11111
    },
    "Q3": 111,
    "Q4": 111,
    "Q5": 111,
    "Q6": {
        "Normal": 111,
        "Good": 111,
        "Bad": 111,
        "Very bad": 111
    }
  }
}
```
  
#### **Task 2:** Develop the forecasting model :
- Predict hourly pollutant concentrations for the following stations and periods, assuming error-free measurements:

````
Station code: 206 | pollutant: SO2   | Period: 2023-07-01 00:00:00 - 2023-07-31 23:00:00
Station code: 211 | pollutant: NO2   | Period: 2023-08-01 00:00:00 - 2023-08-31 23:00:00
Station code: 217 | pollutant: O3    | Period: 2023-09-01 00:00:00 - 2023-09-30 23:00:00
Station code: 219 | pollutant: CO    | Period: 2023-10-01 00:00:00 - 2023-10-31 23:00:00
Station code: 225 | pollutant: PM10  | Period: 2023-11-01 00:00:00 - 2023-11-30 23:00:00
Station code: 228 | pollutant: PM2.5 | Period: 2023-12-01 00:00:00 - 2023-12-31 23:00:00
````

Expected output format:
```json
{
  "target":
  {
    "206": 
      {
        "2023-07-01 00:00:00": 0.32,
        "2023-07-01 01:00:00": 0.5,
        "2023-07-01 02:00:00": 0.8,
        "2023-07-01 03:00:00": 0.11,
        "2023-07-01 04:00:00": 0.7,
        ...
      },
    "211":
      {
        ...
      }
  }
}
```



#### **Task 3:** Detect anomalies in data measurements
Detect instrument anomalies for the following stations and periods:

````
Station code: 205 | pollutant: SO2   | Period: 2023-11-01 00:00:00 - 2023-11-30 23:00:00
Station code: 209 | pollutant: NO2   | Period: 2023-09-01 00:00:00 - 2023-09-30 23:00:00
Station code: 223 | pollutant: O3    | Period: 2023-07-01 00:00:00 - 2023-07-31 23:00:00
Station code: 224 | pollutant: CO    | Period: 2023-10-01 00:00:00 - 2023-10-31 23:00:00
Station code: 226 | pollutant: PM10  | Period: 2023-08-01 00:00:00 - 2023-08-31 23:00:00
Station code: 227 | pollutant: PM2.5 | Period: 2023-12-01 00:00:00 - 2023-12-31 23:00:00
````

Example output:
```json
{
  "target":
  {
    "205": 
    {
      "2023-11-01 00:00:00": 5,
      "2023-11-01 01:00:00": 3,
      "2023-11-01 02:00:00": 6,
      "2023-11-01 03:00:00": 1,
      "2023-11-01 05:00:00": 3,
    ...
    },
    "209":
    {
      ...
    }
  }
}
```

### 💫 Guides
- Study and explore the datasets thoroughly.
- Handle missing or erroneous values.
- Normalize and scale the data.
- Implement feature engineering to improve model accuracy.



## 📤 Submission

Submit a `questions.json` file for the queries in task 1 and a `predictions_task_2.json` and `predictions_task_3.json` files containing the predictions made by your models. Ensure the files are formatted correctly and placed within the `/predictions` folder.


## 📊 Evaluation
- **Task 1:** The questions of this tasks will be evaluated via JSON file, comparing your answer in `questions.json` against the expected value.
- **Task 2:** The model will be evaluated using R2. The score will be the mean for all the station predictions.
- **Task 3:** The model will be evaluated using F1 score, using the macro average.


The grading system will be the following:

- Task 1: 300 / 1400 points
- Task 2: 550 / 1400 points
- Task 3: 550 / 1400 points

**⚠️ Please note:**  
All submissions might undergo a manual code review process to ensure that the work has been conducted honestly and adheres to the highest standards of academic integrity. Any form of dishonesty or misconduct will be addressed seriously, and may lead to disqualification from the challenge.

## ❓ FAQs

#### **Q1: How do I submit my solution?**
A1: Submit your solution via Git. Once your code and predictions are ready, commit your changes to the main branch and push your repository. Your submission will be graded automatically within a few minutes. Make sure to write meaningful commit messages.
