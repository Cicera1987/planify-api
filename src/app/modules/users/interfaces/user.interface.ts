import { Document } from 'mongoose';

export interface IUser extends Document {
    id: string;
    name: string;
    email: string;
    specialty?: string;
    whatsapp?: string;
    isActive: boolean;
}
