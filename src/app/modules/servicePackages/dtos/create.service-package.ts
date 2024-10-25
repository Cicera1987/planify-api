import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";
import { IsArray, IsNotEmpty, IsNumber, IsString } from "class-validator";

export class CreateServicePackageDto {
    @IsString()
    @IsNotEmpty()
    name: string;

    @IsNumber()
    @IsNotEmpty()
    value: number;

    @IsArray()
    @IsNotEmpty({ each: true })
    procedures: IProcedure[];

    constructor(name: string, procedure: IProcedure[] , value: number) {
        this.name = name;
        this.procedures = procedure;
        this.value = value
    }
}