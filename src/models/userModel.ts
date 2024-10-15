import { Schema, model, Document } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';

interface IUser extends Document {
    id: string;
    name: string;
    email: string;
    specialty?: string;
    whatsapp?: string;
    isActive: boolean;
}

// Definição do esquema
const userSchema = new Schema<IUser>({
    id: {
        type: String,
        default: uuidv4,
    },
    name: {
        type: String,
        required: true,
    },
    email: {
        type: String,
        required: true,
        unique: true,
    },
    specialty: {
        type: String,
    },
    whatsapp: {
        type: String,
    },
    isActive: {
        type: Boolean,
        default: true,
    },
}); 

export default model<IUser>('User', userSchema);
