import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";
import mongoose, { Document, Types } from "mongoose";
import { v4 as uuidv4 } from 'uuid';

export type ServicePackageDocument = ServicePackage & Document;

@Schema()
export class ServicePackage {
    @Prop({
        type: String,
        default: uuidv4,
    })
    id: string;
    @Prop({ required: true })
    value: number;

    @Prop({ required: true })
    name: string;

    @Prop({ type: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Procedure' }] })
    procedures: IProcedure[];

    constructor(
        name: string,
        procedures: IProcedure[],
        value: number,
    ) {
        this.id = uuidv4();
        this.name = name;
        this.procedures = procedures;
        this.value = value
    }
}

export const ServicePackageSchema = SchemaFactory.createForClass(ServicePackage);