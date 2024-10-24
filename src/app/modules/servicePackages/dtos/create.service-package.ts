import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";
import { IsNotEmpty } from "class-validator";

export class CreateServicePackageDto {
    @IsNotEmpty()
    name: string;

    @IsNotEmpty()
    procedure: IProcedure[];

    @IsNotEmpty()
    value: number;

    constructor(name: string, procedure: IProcedure[], value: number) {
        this.name = name;
        this.procedure = procedure;
        this.value = value
    }
}