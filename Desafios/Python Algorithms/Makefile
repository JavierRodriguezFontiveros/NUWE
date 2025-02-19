SHELL := /bin/bash

# Ruta del entorno virtual
direnv = $(PWD)/venv-nuwe/

.PHONY: test run

# Crear entorno virtual
$(direnv):
	echo "Setting up virtual environment"
	python3 -m venv $(direnv)
	echo "Correctly setup virtual environment in $(direnv)"
	source $(direnv)/bin/activate && \
	pip install -r requirements.txt

# Ejecutar las pruebas
test: | $(direnv)
	source $(direnv)/bin/activate && \
	$(direnv)/bin/python3 -m pytest

# Ejecutar el archivo principal
run: | $(direnv)
	source $(direnv)/bin/activate && \
	$(direnv)/bin/python3 main.py
