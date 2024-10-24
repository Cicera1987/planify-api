import { Document } from 'mongoose';

export interface IProcedure extends Document {
    id: string;
    name: string;
    value: number;
}
