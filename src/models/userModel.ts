import { Schema, model} from 'mongoose';
import { IUser } from 'src/types/userTypes';
import { v4 as uuidv4 } from 'uuid';

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
