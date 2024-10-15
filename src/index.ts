import mongoose from 'mongoose';
import * as dotenv from 'dotenv';
import app from './app'; // Importando o app

dotenv.config();

const port = process.env.PORT || 5001;
const mongoURI = process.env.MONGODB_URI || '';

if (!mongoURI) {
    console.error('MongoDB URI não está definida no arquivo .env');
    process.exit(1);
}

mongoose.connect(mongoURI)
    .then(() => {
        console.log('Conectado ao MongoDB');
        app.listen(port, () => {
            console.log(`Servidor rodando na porta ${port}`);
        });
    })
    .catch((error) => {
        console.error('Erro ao conectar ao MongoDB:', error);
        process.exit(1);
    });
