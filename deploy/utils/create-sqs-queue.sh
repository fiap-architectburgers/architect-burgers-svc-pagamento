#!/bin/bash

# Verifica se o nome da fila foi fornecido
if [ $# -ne 1 ]; then
  echo "Uso: $0 <nome_da_fila>"
  exit 1
fi

# Nome da fila
queue_name="$1"

# Obtém a URL da fila recém-criada
queue_url=$(aws sqs get-queue-url --queue-name "$queue_name" --query QueueUrl --output text)

# Verifica se a fila existe
if [ -z "$queue_url" ]; then
  # Comando para criar a fila
  aws sqs create-queue --queue-name "$queue_name"

  # Obtém a URL da fila recém-criada
  queue_url=$(aws sqs get-queue-url --queue-name "$queue_name" --query QueueUrl --output text)

  # Verifica se a fila foi criada com sucesso
  if [ -z "$queue_url" ]; then
    echo "Erro ao criar a fila"
    exit 1
  fi
fi

  # Imprime a URL da fila
  echo "$queue_url"