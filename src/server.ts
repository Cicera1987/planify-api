import express from 'express';
import mongoose from 'mongoose';
import * as dotenv from 'dotenv';

// Carrega as variáveis de ambiente do .env
dotenv.config();

const app = express();
const port = process.env.PORT || 5000;

// MongoDB Connection
const mongoURI = process.env.MONGODB_URI || '';

if (!mongoURI) {
    console.error('MongoDB URI is not defined in the .env file');
    process.exit(1); // Encerra a aplicação se a URI não estiver definida
}

mongoose.connect(mongoURI)
    .then(() => {
        console.log('Connected to MongoDB');
    })
    .catch((error) => {
        console.error('Error connecting to MongoDB:', error);
    });

// Middleware e rotas podem ser adicionadas aqui
app.get('/', (req, res) => {
    res.send('API is running');
});

// Inicializa o servidor
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
