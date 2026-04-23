# 🍽️ HungryAdvisor API -- Microservices Architecture

Sistema de recomendação de restaurantes baseado em microserviços com
Spring Boot, Eureka, Spring Cloud e IA (Ollama).

------------------------------------------------------------------------

## 📦 Estrutura do Projeto

    HungryAdvisorApi/
    ├── eureka-server
    ├── user-service
    ├── restaurant-service
    ├── recommendation-service
    └── start-all.sh

------------------------------------------------------------------------

## Como subir o projeto inteiro

### Subir todos os microserviços

``` bash
chmod +x start-all.sh
./start-all.sh
```

### Fazer testes automatizados de recomendações com IA:
``` bash
chmod +x api-teste-pipeline.sh
./api-teste-pipeline.sh
```


### Subir todos os serviços manualmente

#### 1. Eureka Server

``` bash
cd eureka-server
./gradlew clean bootRun
```

 URL: http://localhost:8761

------------------------------------------------------------------------

#### 2. User Service

``` bash
cd user-service
./gradlew clean bootRun
```

 URL: http://localhost:8081

Health: http://localhost:8081/actuator/health

------------------------------------------------------------------------

#### 3. Restaurant Service

``` bash
cd restaurant-service
./gradlew clean bootRun
```

 URL: http://localhost:8082

Health: http://localhost:8082/actuator/health

------------------------------------------------------------------------

#### 4. Recommendation Service (IA)

``` bash
cd recommendation-service
./gradlew clean bootRun
```

 URL: http://localhost:8083

------------------------------------------------------------------------

## IA (Ollama)

Modelo utilizado: - tinyllama

Base URL: http://localhost:11434

Verificar modelos:

``` bash
ollama list
```

Rodar modelo:

``` bash
ollama run tinyllama
```

------------------------------------------------------------------------

## Endpoints principais

### Recommendation Service

#### ➤ Recomendação sem IA

    GET http://localhost:8083/api/recommendations/{userId}

#### ➤ Recomendação com IA

    GET http://localhost:8083/api/recommendations/{userId}/ai

------------------------------------------------------------------------

## Usuários de teste

``` json
[
  {
    "id": "33333333-3333-3333-3333-333333333333",
    "name": "Carlos Lima",
    "favoriteCuisine": "Italiana",
    "city": "Campinas",
    "priceRange": "BAIXO"
  },
  {
    "id": "11111111-1111-1111-1111-111111111111",
    "name": "João Silva",
    "favoriteCuisine": "Brasileira",
    "city": "São Paulo",
    "priceRange": "MEDIO"
  },
  {
    "id": "22222222-2222-2222-2222-222222222222",
    "name": "Marina Costa",
    "favoriteCuisine": "Japonesa",
    "city": "São Paulo",
    "priceRange": "ALTO"
  }
]
```

------------------------------------------------------------------------

## Testes com CURL

### Recomendação base

``` bash
curl http://localhost:8083/api/recommendations/11111111-1111-1111-1111-111111111111
```

### Recomendação com IA

``` bash
curl http://localhost:8083/api/recommendations/11111111-1111-1111-1111-111111111111/ai
```

------------------------------------------------------------------------

## Pipeline de testes completo

``` bash
for id in 11111111-1111-1111-1111-111111111111 22222222-2222-2222-2222-222222222222 33333333-3333-3333-3333-333333333333
do
  echo "=============================="
  echo "Testando usuário $id"
  echo "------------------------------"

  curl -s http://localhost:8083/api/recommendations/$id
  echo ""

  curl -s http://localhost:8083/api/recommendations/$id/ai
  echo ""
done
```

------------------------------------------------------------------------

## Postman

### ✔️ Collection base

-   GET Recomendação\
    `http://localhost:8083/api/recommendations/{{userId}}`

-   GET IA\
    `http://localhost:8083/api/recommendations/{{userId}}/ai`

### ✔️ Variável

    userId = 11111111-1111-1111-1111-111111111111

------------------------------------------------------------------------

## Eureka Dashboard

http://localhost:8761

------------------------------------------------------------------------

## Health Checks

``` bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8083/actuator/health
```

------------------------------------------------------------------------

## Parar tudo

``` bash
pkill -f spring
```

------------------------------------------------------------------------

## Problemas comuns

### Eureka não sobe

-   verifique porta 8761

### IA retorna erro

``` bash
ollama run tinyllama
```

### Serviço não registra no Eureka

-   aguarde Eureka subir primeiro

------------------------------------------------------------------------

## Status esperado

-   Eureka: UP
-   User Service: UP
-   Restaurant Service: UP
-   Recommendation Service: UP
-   IA: funcionando via Ollama
