import os
import zipfile

def create_lambda_zip(lambda_file_path, zip_filename):
    # Crear el archivo .zip
    with zipfile.ZipFile(zip_filename, 'w', zipfile.ZIP_DEFLATED) as zipf:
        # Agregar el archivo lambda.py
        zipf.write(lambda_file_path, os.path.basename(lambda_file_path))
    
    print(f"Lambda zip creado: {zip_filename}")

# Ruta de tu archivo Python Lambda (en el mismo directorio)
lambda_file = 'lambda_function.py'  # Asumiendo que el archivo se llama 'lambda_function.py'

# Ruta donde se guardará el archivo zip (en el mismo directorio)
zip_file_path = 'lambda.zip'

# Crear el archivo zip
create_lambda_zip(lambda_file, zip_file_path)

