import { Prop, Schema, SchemaFactory } from "@nestjs/mongoose";
import { Procedure } from "app/modules/procedures/entities/procedure.entity";
import { Document, Types } from "mongoose";
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
    name: string;

    @Prop({ type: [{ type: Types.ObjectId, ref: 'Procedure' }] })
    procedures: Procedure[];

    @Prop({ required: true })
    value: number;

    constructor(
        name: string,
        procedure: Procedure[],
        value: number,
    ) {
        this.id = uuidv4();
        this.name = name;
        this.procedures = procedure;
        this.value = value
    }
}

export const ServicePackageSchema = SchemaFactory.createForClass(ServicePackage);