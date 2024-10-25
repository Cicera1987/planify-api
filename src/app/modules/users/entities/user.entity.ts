import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { HydratedDocument } from 'mongoose';
import { IUser } from '../interfaces/user.interface';
import { v4 as uuidv4 } from 'uuid';
import { iconUser } from '../../../../assets/icons/iconUser';

export type UserDocument = HydratedDocument<IUser>;

@Schema()
export class User {
  @Prop({
    type: String,
    default: uuidv4,
    unique: true,
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

  @Prop({ default: iconUser })
  image: string;

  constructor(
    name: string,
    email: string,
    specialty?: string,
    phone?: string,
    image?: string,
  ) {
    this.id = uuidv4();
    this.name = name;
    this.email = email;
    this.specialty = specialty;
    this.phone = phone;
    this.isActive = true;
    this.image = image || iconUser;
  }
}
export const UserSchema = SchemaFactory.createForClass(User);
