import { HydratedDocument } from "mongoose";
import { IAuth } from "../interfaces/auth.interface";
import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";

export type AuthDocument = HydratedDocument<IAuth>;

@Schema()
export class AuthEntity {
    @Prop({
        type: String,
        required: true,
        unique: true,
    })
    id: string;

    @Prop({ required: true })
    username: string;

    @Prop({ required: true, unique: true })
    email: string;

    @Prop({ required: true })
    password: string;

    constructor(id: string, username: string, email: string, password: string) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

}

export const AuthSchema = SchemaFactory.createForClass(AuthEntity);