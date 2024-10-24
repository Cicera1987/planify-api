import { IsEmail, IsNotEmpty, IsOptional } from "class-validator";

export class CreateClientDto {
    @IsNotEmpty()
    name:string;

    @IsEmail()
    email?:string;

    @IsOptional()
    gender?: string;

    @IsOptional()
    phone?: string;

    @IsOptional()
    image?: string;

    constructor(name: string, email: string, gender: string, phone: string, image: string ){
        this.name = name;
        this.email = email;
        this.gender = gender,
        this.phone = phone,
        this.image = image
    }
}