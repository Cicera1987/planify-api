import { Document } from 'mongoose';

export interface IUser extends Document {
  id: string;
  name: string;
  email: string;
  specialty?: string;
  phone?: string;
  isActive: boolean;
  image?: string;
}
