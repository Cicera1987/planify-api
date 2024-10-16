import { IsEmail, IsNotEmpty, IsOptional } from 'class-validator';

export class CreateUserDto {
    @IsNotEmpty()
    name: string;

    @IsEmail()
    email: string;

    @IsOptional()
    specialty?: string;

    @IsOptional()
    whatsapp?: string;

    constructor(name: string, email: string, specialty?: string, whatsapp?: string) {
        this.name = name;
        this.email = email;
        this.specialty = specialty;
        this.whatsapp = whatsapp;
    }
}
