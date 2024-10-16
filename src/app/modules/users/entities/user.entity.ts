import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';
import { IUser } from '../interfaces/user.interface';
import { v4 as uuidv4 } from 'uuid';

export type UserDocument = HydratedDocument<IUser>;

@Schema()
export class User {
    @Prop({
        type: String,
        default: uuidv4,
    })
    id: string;

    @Prop({ required: true })
    name: string;

    @Prop({ required: true, unique: true })
    email: string;

    @Prop()
    specialty?: string;

    @Prop()
    phone?: string;

    @Prop({ default: true })
    isActive: boolean;

    constructor(name: string, email: string, specialty?: string, phone?: string) {
        this.id = uuidv4();
        this.name = name;
        this.email = email;
        this.specialty = specialty;
        this.phone = phone;
        this.isActive = true;
    }
}
export const UserSchema = SchemaFactory.createForClass(User);

