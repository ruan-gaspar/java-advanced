#!/bin/bash

if [ -f pids.txt ]; then
  kill $(cat pids.txt)
  echo "🛑 Todos os microserviços foram encerrados."
  rm pids.txt
else
  echo "Nenhum PID encontrado."
fi
