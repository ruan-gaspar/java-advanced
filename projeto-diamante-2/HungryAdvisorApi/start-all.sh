#!/bin/bash

BASE_DIR="$HOME/Área de Trabalho/java-advanced/projeto-diamante-2/HungryAdvisorApi"

echo "🚀 Subindo Eureka Server..."
cd "$BASE_DIR/eureka-server"
./gradlew bootRun > eureka.log 2>&1 &
EUREKA_PID=$!

sleep 5

echo "🚀 Subindo User Service..."
cd "$BASE_DIR/user-service"
./gradlew bootRun > user.log 2>&1 &
USER_PID=$!

sleep 5

echo "🚀 Subindo Restaurant Service..."
cd "$BASE_DIR/restaurant-service"
./gradlew bootRun > restaurant.log 2>&1 &
RESTAURANT_PID=$!

sleep 5

echo "🚀 Subindo Recommendation Service..."
cd "$BASE_DIR/recommendation-service"
./gradlew bootRun > recommendation.log 2>&1 &
RECOMMENDATION_PID=$!

echo ""
echo "✅ Todos os serviços estão subindo!"
echo "------------------------------------"
echo "Eureka Server PID: $EUREKA_PID"
echo "User Service PID: $USER_PID"
echo "Restaurant Service PID: $RESTAURANT_PID"
echo "Recommendation Service PID: $RECOMMENDATION_PID"
echo "------------------------------------"

echo $EUREKA_PID $USER_PID $RESTAURANT_PID $RECOMMENDATION_PID > pids.txt

echo "🧾 Logs:"
echo "  eureka.log"
echo "  user.log"
echo "  restaurant.log"
echo "  recommendation.log"
