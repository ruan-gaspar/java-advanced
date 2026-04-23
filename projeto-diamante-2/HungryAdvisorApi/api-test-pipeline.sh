#!/bin/bash

BASE_URL="http://localhost:8083/api/recommendations"

echo "======================================"
echo "🚀 PIPELINE DE TESTES - RECOMMENDATION"
echo "======================================"

USERS=(
"11111111-1111-1111-1111-111111111111"
"22222222-2222-2222-2222-222222222222"
"33333333-3333-3333-3333-333333333333"
)

for USER in "${USERS[@]}"
do
  echo ""
  echo "--------------------------------------"
  echo "👤 Testando usuário: $USER"
  echo "--------------------------------------"

  echo "📌 Recomendação base:"
  curl -s "$BASE_URL/$USER" | jq

  echo ""
  echo "🤖 Recomendação com IA:"
  curl -s "$BASE_URL/$USER/ai" | jq

  echo ""
  sleep 2
done

echo ""
echo "======================================"
echo "✅ PIPELINE FINALIZADA"
echo "======================================"


