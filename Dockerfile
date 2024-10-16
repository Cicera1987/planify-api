FROM node:20

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install

# Instala o Nest CLI globalmente
RUN npm install -g @nestjs/cli

COPY . .

EXPOSE 5000

CMD ["npm", "run", "dev"]
