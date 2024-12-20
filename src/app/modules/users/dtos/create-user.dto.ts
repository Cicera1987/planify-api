import { IsEmail, IsNotEmpty, IsOptional } from 'class-validator';

export class CreateUserDto {
  @IsNotEmpty()
  name: string;

  @IsEmail()
  email: string;

  @IsNotEmpty()
  password: string;

  @IsOptional()
  specialty?: string;

  @IsOptional()
  phone?: string;

  @IsOptional()
  image?: string;

  constructor(
    name: string,
    email: string,
    password: string,
    specialty?: string,
    phone?: string,
    image?: string,
  ) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.specialty = specialty;
    this.phone = phone;
    this.image = image;
  }
}
