# TripFinder

## Vídeo demonstrativo

> Adicione aqui o link do vídeo demonstrativo do projeto  
> Exemplo: `https://www.youtube.com/watch?v=SEU_VIDEO`

---

## Sobre o projeto

O **TripFinder** é uma aplicação full stack desenvolvida para pesquisar pontos turísticos e locais de interesse utilizando a API externa **OpenTripMap**.

O projeto é composto por:

- **Backend Java 17 com Spring Boot**
- **Frontend Angular**
- **Autenticação com JWT Bearer Token**
- **Integração com OpenTripMap**
- **Persistência de usuários e favoritos**
- **Upload de foto de perfil**
- **Documentação com Swagger / OpenAPI**

A proposta do sistema é permitir que o usuário:

- criar conta e fazer login
- pesquisar lugares por **cidade**
- pesquisar lugares por **proximidade**, usando a localização atual do dispositivo
- visualizar detalhes de um local
- salvar lugares favoritos
- gerenciar seu próprio perfil
- alterar **foto**, **e-mail** e **senha**
- consumir uma API Java segura com autenticação JWT no frontend Angular

---

## Funcionalidades

### Backend
- API REST em **Java 17 + Spring Boot**
- autenticação e autorização com **JWT**
- integração com a API **OpenTripMap**
- documentação automática com **Swagger/OpenAPI**
- tratamento de rotas protegidas com Spring Security
- upload e exposição de imagens de perfil em `/uploads/**`
- persistência de usuários e favoritos
- validações de entrada para busca, autenticação e perfil

### Frontend
- tela de **login**
- tela de **cadastro**
- tela inicial **Home**
- busca por **cidade**
- busca por **proximidade**
- visualização de **detalhes do local**
- tela de **favoritos**
- tela de **perfil do usuário**
- uso de **localStorage** para armazenamento do token JWT
- uso de **interceptor** para envio automático do Bearer Token
- uso de **auth guard** para proteção das rotas autenticadas

---

## Observação importante sobre a interface

Durante a evolução do projeto, a antiga tela/classe **Search** foi descontinuada por decisão de arquitetura.

Suas funcionalidades foram incorporadas à tela **Home**, que passou a concentrar:

- a busca por cidade
- a busca por proximidade
- os cards de categorias
- a navegação principal do usuário autenticado

Isso simplifica a experiência do usuário e centraliza a descoberta de lugares em uma única tela.

---

## Arquitetura resumida

### Backend
O backend recebe as requisições do frontend Angular, valida os parâmetros, autentica o usuário quando necessário e consulta a API da **OpenTripMap** por meio de uma abstração `PlaceProviderClient`.

Fluxo resumido:

1. O frontend faz a requisição para a API Java
2. A API valida os dados enviados
3. O Spring Security verifica o token JWT nas rotas protegidas
4. O serviço consulta a OpenTripMap
5. Os dados são tratados e transformados em DTOs
6. O frontend exibe os resultados

### Frontend
O frontend Angular consome a API Java e fornece a interface visual da aplicação.

Fluxo resumido:

1. Usuário faz login ou cadastro
2. Token JWT é salvo no `localStorage`
3. Interceptor envia o token no header `Authorization`
4. Usuário navega entre Home, Favoritos, Perfil e Detalhes
5. As rotas protegidas exigem autenticação

---

## Tecnologias utilizadas

### Backend
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- Spring Data JPA
- JWT
- OpenAPI / Swagger
- Multipart File Upload
- Banco de dados relacional
- OpenTripMap API

### Frontend
- Angular
- TypeScript
- Angular Router
- Reactive Forms
- HttpClient
- Auth Guard
- Http Interceptor
- localStorage

### Ferramentas
- Docker
- Docker Compose
- Swagger UI
- Node.js / npm
- Maven ou Gradle
- Git / GitHub

---

## Estrutura funcional do projeto

### Módulos principais do backend
- autenticação e registro de usuário
- atualização de perfil
- upload de foto
- busca de lugares
- favoritos
- segurança JWT
- integração com OpenTripMap
- documentação OpenAPI

### Módulos principais do frontend
- login
- cadastro
- home
- detalhes do lugar
- favoritos
- perfil
- services de autenticação, favoritos e lugares
- storage service
- interceptor e guard

---

## Autenticação

A autenticação do sistema é feita com **JWT Bearer Token**.

Após login ou cadastro, o backend retorna um token JWT.  
O frontend salva esse token no `localStorage` e passa a enviá-lo automaticamente nas próximas requisições autenticadas.

Header esperado nas rotas protegidas:

```http
Authorization: Bearer SEU_TOKEN