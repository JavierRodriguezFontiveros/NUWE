#!/bin/bash

echo "🔄 Iniciando el pipeline..."

# 1. Procesar datos
echo "📊 Procesando datos..."
python3 src/data_processing.py

# 2. Entrenar modelo
echo "🤖 Entrenando modelo..."
python3 src/model_training.py

# 3. Hacer predicciones
echo "📈 Generando predicciones..."
python3 src/model_prediction.py

echo "✅ Pipeline completado. Las predicciones están en predictions/predictions.json"
