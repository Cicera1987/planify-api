import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';
import { IClient } from '../interfaces/client.interface';
import { v4 as uuidv4 } from 'uuid';
import { iconUser } from '../../../../assets/icons/iconUser';
import { IPackageMonthly } from 'app/modules/servicePackages/interfaces/service-package.interface';

export type ClientDocument = HydratedDocument<IClient>;

@Schema()
export class Client {
  @Prop({
    type: String,
    default: uuidv4,
    unique: true, 
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

  @Prop({ required: true, default: false })
  statusPackage: boolean;

  @Prop()
  package?: IPackageMonthly[];

  constructor(
    name: string,
    email: string,
    statusPackage: boolean,
    gender?: string,
    phone?: string,
    image?: string,
    packageData?: IPackageMonthly[],
  ) {
    this.id = uuidv4();
    this.name = name;
    this.email = email;
    this.statusPackage = statusPackage;
    this.gender = gender;
    this.phone = phone;
    this.image = image || iconUser;
    this.package = packageData;
  }
}
export const ClientSchema = SchemaFactory.createForClass(Client);
