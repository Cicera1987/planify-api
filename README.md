# Planify API

## Descrição

Este projeto é uma API para gerenciamento de agendamentos, permitindo que usuários façam reservas e administrem seus serviços de forma eficiente. A API é construída utilizando o framework [NestJS](https://nestjs.com/), e se conecta a um banco de dados MongoDB para armazenar as informações dos usuários e seus agendamentos.

## Pré-requisitos

Certifique-se de ter o seguinte instalado:

- [Node.js](https://nodejs.org/) (versão 14 ou superior)
- [Nest CLI](https://docs.nestjs.com/cli/overview) (opcional, para desenvolvimento mais fácil)
- [Docker](https://www.docker.com/) (opcional, se você quiser rodar com contêineres)
- [MongoDB](https://www.mongodb.com/)

## Instalação

1. Clone este repositório:

   ```bash  
   git clone https://github.com/Cicera1987/planify-api
   cd planify-api


2. Instale as dependências:
  ```bash
   npm install

3.Crie o arquivo .env na raiz do projeto com as seguintes variáveis:
   PORT=5000
   MONGODB_URI=mongodb://localhost:27017/planify

4. Execute o projeto:
   npm run start:dev

## Estrutura do Projeto
# A estrutura básica do projeto é baseada na arquitetura modular do NestJS:

## Módulos: 
A API é organizada em módulos (como AgendamentosModule, ServicosModule, UsuariosModule), facilitando a escalabilidade e manutenção.
Controladores: Definem os endpoints da API.
Serviços: Contêm a lógica de negócio da aplicação.
Repositórios: Para a interação com o banco de dados MongoDB usando o Mongoose.
Endpoints Principais
Agendamentos: CRUD de agendamentos, incluindo criação, edição, cancelamento e visualização de horários.
Usuários: Cadastro e gerenciamento de usuários.
Serviços: Definição e administração de tipos de serviços e pacotes.
Executando com Docker (Opcional)
Se preferir utilizar contêineres, você pode rodar o projeto com Docker:


# Certifique-se de que o Docker está instalado e rodando.

1. Construa a imagem Docker:
docker build -t planify-api .


2. Execute o contêiner:
docker run -p 5000:5000 planify-api
Isso vai disponibilizar a API em http://localhost:5000.

