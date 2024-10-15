# Usando a imagem oficial do Node.js como base
FROM node:20

# Criar um diretório de trabalho no contêiner
WORKDIR /usr/src/app

# Copiar package.json e package-lock.json para o contêiner
COPY package*.json ./

# Instalar as dependências
RUN npm install

# Copiar o restante dos arquivos do projeto para o contêiner
COPY . .

# Expor a porta que o aplicativo usará
EXPOSE 5000

# Comando para iniciar a aplicação
CMD ["npm", "run", "dev"]
