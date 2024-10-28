import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { IClient } from 'app/modules/clients/interfaces/client.interface';
import { IProcedure } from 'app/modules/procedures/interfaces/procedure.interface';
import mongoose, { HydratedDocument } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';

export type SchedulingreDocument = HydratedDocument<Scheduling>;

@Schema()
export class Scheduling {
  @Prop({
    type: String,
    default: uuidv4,
  })
  id: string;

  @Prop({ required: true })
  date: Date;

  @Prop({ type: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Procedure' }] })
  procedure: IProcedure[];

  @Prop({ type: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Client' }] })
  client: IClient[];

  constructor(date: Date, procedure: IProcedure[], client: IClient[]) {
    this.id = uuidv4();
    this.date = date;
    this.procedure = procedure;
    this.client = client;
  }
}
export const SchedulingSchema = SchemaFactory.createForClass(Scheduling);
