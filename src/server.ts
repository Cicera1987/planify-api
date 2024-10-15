import express from 'express';
import mongoose from 'mongoose';
import * as dotenv from 'dotenv';
import userRoutes from './routes/userRouters';

dotenv.config();

const app = express();
const port = process.env.PORT || 5001;
const mongoURI = process.env.MONGODB_URI || '';

if (!mongoURI) {
    console.error('MongoDB URI não está definida no arquivo .env');
    process.exit(1);
}

mongoose.connect(mongoURI)
    .then(() => console.log('Conectado ao MongoDB'))
    .catch((error) => {
        console.error('Erro ao conectar ao MongoDB:', error);
        process.exit(1);
    });

app.use(express.json());

// Rotas de usuários
app.use('/users', userRoutes);


app.listen(port, () => {
    console.log(`Servidor rodando na porta ${port}`);
});
