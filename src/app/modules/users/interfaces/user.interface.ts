import { Document } from 'mongoose';

export interface IUser extends Document {
  id: string;
  name: string;
  email: string;
  password: string;
  specialty?: string;
  phone?: string;
  isActive: boolean;
  image?: string;
  isAdmin: boolean;
}

export interface IRegister {
  name: string;
  email: string;
  password: string;
  isActive?: boolean;
  isAdmin?: boolean;
}

