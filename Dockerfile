# Usar uma imagem Node.js como base
FROM node:18-alpine

# Definir o diretório de trabalho dentro do container
WORKDIR /usr/src/app

# Copiar o package.json e o package-lock.json para o container
COPY package*.json ./

# Instalar as dependências
RUN npm install

# Copiar todos os arquivos do projeto para o diretório de trabalho
COPY . .

# Compilar o TypeScript para JavaScript
RUN npm run build

# Expor a porta 3000 para o container
EXPOSE 3000

# Definir o comando padrão para iniciar o app
CMD ["npm", "run", "start:prod"]
