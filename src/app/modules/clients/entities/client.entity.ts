import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';
import { IClient } from '../interfaces/client.interface';
import { v4 as uuidv4 } from 'uuid';
import { iconUser } from '../../../../assets/icons/iconUser';


export type ClientDocument = HydratedDocument<IClient>;

@Schema()
export class Client {
    @Prop({
        type: String,
        default: uuidv4,
    })
    id: string;

    @Prop({ required: true })
    name: string;

    @Prop({ required: true, unique: true })
    email?: string;

    @Prop()
    gender?: string;

    @Prop()
    phone?: string;

    @Prop({ default: iconUser })
    image: string;

    constructor(
        name: string,
        email?: string,
        gender?: string,
        phone?: string,
        image?: string
    ) {
        this.id = uuidv4();
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.image = image || iconUser;
    }
}
export const ClientSchema = SchemaFactory.createForClass(Client);