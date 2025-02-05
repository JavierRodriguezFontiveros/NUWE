print("nothing")

def train_model(X_train, y_train):
    """
    Inicializar y entrenar el modelo.
    """
    # Usamos un RandomForestClassifier, pero puedes probar otros modelos
    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)
    
    return model

# def train_model(X_train, y_train): 
#     """
#     Inicializar y entrenar el modelo con parámetros ajustados.
#     """
#     # Ajuste de hiperparámetros
#     model = RandomForestClassifier(
#         n_estimators=200,           # Más árboles
#         max_depth=5,               # Profundidad máxima para evitar sobreajuste
#         min_samples_split=4,        # Número mínimo de muestras para dividir un nodo
#         min_samples_leaf=2,         # Número mínimo de muestras por hoja
#         max_features='sqrt',        # Usar la raíz cuadrada del número total de características
#         random_state=42
#     )
#     model.fit(X_train, y_train)