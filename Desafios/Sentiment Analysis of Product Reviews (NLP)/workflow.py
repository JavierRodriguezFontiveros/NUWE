import pandas as pd
import gensim.downloader as api
import numpy as np
import re
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report, f1_score
import json

# Descargar stopwords de NLTK
nltk.download('stopwords')
nltk.download('wordnet')

# Cargar el modelo preentrenado de FastText
model = api.load("glove-wiki-gigaword-100")

# Función para obtener el vector promedio de un texto
def get_average_vector(text, model):
    tokens = text.split()  # Tokenizamos el texto
    vectors = [model[word] for word in tokens if word in model]  # Conseguimos el vector de cada palabra
    if len(vectors) > 0:
        return sum(vectors) / len(vectors)  # Promediamos los vectores
    else:
        return np.zeros(model.vector_size)  # Si no hay vectores, devolvemos un vector de ceros

# Función para limpiar el texto
def clean_text(text):
    # Convertir a minúsculas
    text = text.lower()
    # Eliminar caracteres especiales, números y puntuación
    text = re.sub(r'[^a-záéíóúñ\s]', '', text)
    return text

# Función para eliminar stopwords y lematizar
lemmatizer = WordNetLemmatizer()
stop_words = set(stopwords.words('spanish'))

def preprocess_text(text):
    words = text.split()
    # Eliminar stopwords y lematizar
    cleaned_words = [lemmatizer.lemmatize(word) for word in words if word not in stop_words]
    return ' '.join(cleaned_words)

# Cargar y limpiar los conjuntos de datos
train = pd.read_csv('train/train.csv', sep=';')
test = pd.read_csv('test/test.csv', sep=';')

# Llenar valores nulos en 'Summary' y 'Text' con cadenas vacías
train['Summary'] = train['Summary'].fillna('')
train['Text'] = train['Text'].fillna('')

# Crear la columna 'Review' concatenando 'Summary' y 'Text'
train['Review'] = train['Summary'] + ' ' + train['Text']

test['Summary'] = test['Summary'].fillna('')
test['Text'] = test['Text'].fillna('')
test['Review'] = test['Summary'] + ' ' + test['Text']

# Limpiar el texto en la columna 'Review'
train['Review'] = train['Review'].apply(clean_text)
test['Review'] = test['Review'].apply(clean_text)

# Aplicar la función de preprocesamiento al texto (eliminar stopwords y lematizar)
train['Review'] = train['Review'].apply(preprocess_text)
test['Review'] = test['Review'].apply(preprocess_text)

# Verifica que las columnas 'Review' se han creado correctamente
print(train.columns)  # Debería mostrar 'Review'
print(test.columns)   # Debería mostrar 'Review'

# Aplicar la función para obtener el vector promedio de las reseñas
train['Review_Vector'] = train['Review'].apply(lambda x: get_average_vector(x, model))
test['Review_Vector'] = test['Review'].apply(lambda x: get_average_vector(x, model))

# Verificar que las nuevas columnas han sido agregadas correctamente
print(train[['Review', 'Review_Vector']].head())  # Muestra las primeras filas para validar
print(test[['Review', 'Review_Vector']].head())   # Muestra las primeras filas para validar

# ---- Añadir el vectorizador TF-IDF ----

# Inicializar el vectorizador TF-IDF
vectorizer = TfidfVectorizer(max_features=5000)  # Limitar a 5000 características

# Ajustar y transformar el texto del conjunto de entrenamiento
X_tfidf = vectorizer.fit_transform(train['Review'])

# Ver las primeras características
print(f"Dimensiones de la matriz TF-IDF: {X_tfidf.shape}")


# ---- Añadir la longitud del texto como nueva característica ----

# Crear una nueva característica 'Length' que es la longitud del texto
train['Length'] = train['Review'].apply(len)
test['Length'] = test['Review'].apply(len)

# Ver el resultado
print(train[['Review', 'Length', 'Score']].head())  # Muestra las primeras filas

# ---- Inicializar el vectorizador con n-gramas (unigrams, bigrams y trigrams) ----

# Inicializar el vectorizador con bigramas y trigramas
vectorizer_ngram = TfidfVectorizer(ngram_range=(1, 3), max_features=5000)

# Ajustar y transformar el texto del conjunto de entrenamiento
X_ngram = vectorizer_ngram.fit_transform(train['Review'])

# Ver las dimensiones de la matriz de n-gramas
print(f"Dimensiones de la matriz N-gramas: {X_ngram.shape}")


# ---- Dividir los datos en entrenamiento y validación (70%/30%) ----

# Tomar las características de TF-IDF (o de n-gramas si lo prefieres)
X = X_tfidf  # O puedes usar X_ngram

# La variable objetivo (Score)
y = train['Score']

# Dividir el conjunto de datos en train y validación (70% / 30%)
X_train, X_valid, y_train, y_valid = train_test_split(X, y, test_size=0.3, random_state=42)

# Ver las dimensiones
print(f"Entrenamiento X: {X_train.shape}, Y: {y_train.shape}")
print(f"Validación X: {X_valid.shape}, Y: {y_valid.shape}")


# ---- Entrenamiento del modelo ----

# Inicializar el clasificador Random Forest
model = RandomForestClassifier(class_weight='balanced', random_state=42)

# Entrenar el modelo con los datos de entrenamiento
model.fit(X_train, y_train)

# Realizar predicciones en el conjunto de validación
y_pred = model.predict(X_valid)

# Evaluar el modelo usando F1 Score
f1 = f1_score(y_valid, y_pred, average='weighted')  # F1 ponderado
print(f"F1 Score: {f1:.4f}")

# Ver el reporte de clasificación
print(classification_report(y_valid, y_pred))


# ---- Predicciones en el conjunto de test ----

# Preprocesar las reseñas del conjunto de test de la misma manera
test['Review'] = test['Summary'] + ' ' + test['Text']
test['Review'] = test['Review'].apply(clean_text)
test['Review'] = test['Review'].apply(preprocess_text)

# Vectorizar las reseñas del conjunto de test
X_test = vectorizer.transform(test['Review'])  # Usamos el mismo vectorizador que en el entrenamiento

# Realizar las predicciones para el test
predictions = model.predict(X_test)

# Crear el archivo JSON con las predicciones
# Convertir los valores a int (tipo nativo de Python)
predictions_dict = {k: int(v) for k, v in zip(test['Test_id'], predictions)}

# Guardar las predicciones en el archivo predictions.json
with open('predictions/predictions.json', 'w') as f:
    json.dump({"target": predictions_dict}, f, indent=4)

print("Archivo predictions.json creado exitosamente.")
