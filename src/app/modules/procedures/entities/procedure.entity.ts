import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';
import { IProcedure } from '../interfaces/procedure.interface';

export type ProcedureDocument = HydratedDocument<IProcedure>;

@Schema()
export class Procedure {
    @Prop({
        type: String,
        default: uuidv4,
    })
    id: string;

    @Prop({ required: true })
    name: string;


    @Prop({ required: true })
    value: number;


    constructor(
        name: string,
        value: number,
    ) {
        this.id = uuidv4();
        this.name = name;
        this.value = value;
    }
}
export const ProcedureSchema = SchemaFactory.createForClass(Procedure);
