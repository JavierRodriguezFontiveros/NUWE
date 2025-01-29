# NUWE

¡Entiendo! Quieres usar Jenkins para automatizar la generación del archivo predictions.json en tu hackatón. Vamos a hacerlo paso a paso. 🚀

📌 Paso 1: Configurar Jenkins con tu repositorio del hackatón
Antes de crear el pipeline, necesitamos conectar Jenkins con el repositorio donde tienes tu código.

1️⃣ Abre Jenkins en http://localhost:8080.
2️⃣ Haz clic en "New Item" (Nuevo Proyecto).
3️⃣ Escribe un nombre para el proyecto, por ejemplo: "Hackathon_Pipeline".
4️⃣ Selecciona "Pipeline" y haz clic en OK.

📌 Paso 2: Conectar Jenkins con tu repositorio GitHub
1️⃣ En la configuración del proyecto, ve a la sección "Pipeline".
2️⃣ En Definition, selecciona "Pipeline script from SCM".
3️⃣ En SCM, elige Git.
4️⃣ En Repository URL, ingresa la URL de tu repo GitHub.

Ejemplo: https://github.com/tuusuario/hackathon-repo.git
5️⃣ En Branch, pon main o la rama donde está tu código.
6️⃣ En Script Path, escribe Jenkinsfile.
🔹 Esto le dice a Jenkins que busque el archivo Jenkinsfile en tu repo y lo ejecute cuando hagas git push.

✅ Guarda los cambios haciendo clic en Save.

📌 Paso 3: Escribir el Jenkinsfile
Ahora, debemos crear un archivo llamado Jenkinsfile en la raíz de tu repositorio GitHub.
Este archivo le dirá a Jenkins cómo ejecutar el pipeline.

Abre VS Code y crea un archivo llamado Jenkinsfile. Copia y pega este código:

groovy
Copiar
Editar
pipeline {
    agent any  // Usa cualquier máquina disponible para correr el pipeline

    stages {
        stage('Preparar Entorno') {
            steps {
                sh 'pip install -r requirements.txt'  // Instala dependencias
            }
        }
        stage('Entrenar Modelo') {
            steps {
                sh 'python model.py'  // Ejecuta el script de entrenamiento
            }
        }
        stage('Generar Predicciones') {
            steps {
                sh 'python generate_predictions.py'  // Genera el predictions.json
            }
        }
        stage('Guardar Predicciones') {
            steps {
                archiveArtifacts artifacts: 'predictions/predictions.json', fingerprint: true
            }
        }
    }
}
📌 Paso 4: Asegurar que tu código tenga todo lo necesario
Para que el pipeline funcione, tu repo debe incluir:

✅ model.py → Código que entrena el modelo
✅ generate_predictions.py → Código que genera predictions.json
✅ requirements.txt → Lista de dependencias de Python

Ejemplo de un requirements.txt:

Copiar
Editar
pandas
numpy
scikit-learn
🔹 Sube todo esto a GitHub (git add . && git commit -m "Añadir Jenkinsfile" && git push).

📌 Paso 5: Ejecutar el Pipeline
1️⃣ Ve a Jenkins y abre el proyecto "Hackathon_Pipeline".
2️⃣ Haz clic en "Build Now".
3️⃣ Ve a "Console Output" para ver los logs.
4️⃣ Si todo está bien, verás un mensaje como:
✅ Predicciones guardadas en predictions.json

📌 Paso 6: Descargar el predictions.json generado
1️⃣ En Jenkins, entra al proyecto "Hackathon_Pipeline".
2️⃣ Haz clic en el último "Build".
3️⃣ Busca la sección "Artifacts" y descarga predictions.json.

📌 Paso 7 (Opcional): Hacer que Jenkins se ejecute cada vez que hagas un push
Si quieres que Jenkins se ejecute automáticamente cada vez que subas código, sigue estos pasos:

🔹 En GitHub
1️⃣ Ve a Settings > Webhooks en tu repositorio.
2️⃣ Haz clic en Add webhook.
3️⃣ En Payload URL, pon la URL de tu Jenkins:

arduino
Copiar
Editar
http://TU_IP:8080/github-webhook/
4️⃣ En Content Type, elige application/json.
5️⃣ En Which events?, selecciona "Just the push event".
6️⃣ Guarda los cambios.

🔹 En Jenkins
1️⃣ Ve a la configuración de tu proyecto.
2️⃣ Activa la opción "GitHub hook trigger for GITScm polling".
3️⃣ Guarda los cambios.

Ahora, cada vez que hagas git push, Jenkins correrá automáticamente el pipeline. 🚀🎉

📌 Resumen Final
✅ Jenkins está conectado a tu repo GitHub
✅ Pipeline instala dependencias, entrena el modelo y genera predictions.json
✅ Puedes descargar predictions.json desde Jenkins
✅ (Opcional) Jenkins se ejecuta automáticamente en cada git push

🚀 ¡Ya tienes un pipeline funcional para tu hackatón! ¿Quieres agregar pruebas automáticas o Docker al pipeline? 😃