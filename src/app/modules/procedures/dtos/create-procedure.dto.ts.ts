import {IsNotEmpty } from 'class-validator';

export class CreateProcedureDto {
    @IsNotEmpty()
    name: string;

    @IsNotEmpty()
    value: number;

    constructor(name: string, value: number) {
        this.name = name;
        this.value = value;;
    }
}
