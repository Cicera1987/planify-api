import mongoose from 'mongoose';
import * as dotenv from 'dotenv';

dotenv.config();

const mongoURI = process.env.MONGODB_URI || '';

if (!mongoURI) {
    console.error('MongoDB URI não está definida no arquivo .env');
    process.exit(1);
}

const connectToDatabase = async () => {
    try {
        await mongoose.connect(mongoURI);
        console.log('Conectado ao MongoDB');
    } catch (error) {
        console.error('Erro ao conectar ao MongoDB:', error);
        process.exit(1);
    }
};

export default connectToDatabase;

