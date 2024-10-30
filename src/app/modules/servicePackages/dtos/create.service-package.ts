import { IProcedure } from 'app/modules/procedures/interfaces/procedure.interface';
import { IsArray, IsBoolean, IsNotEmpty, IsNumber, IsString } from 'class-validator';

export class CreateServicePackageDto {
  @IsString()
  @IsNotEmpty()
  name: string;

  @IsNumber()
  @IsNotEmpty()
  value: number;

  @IsBoolean()
  @IsNotEmpty()
  isActive: boolean;

  @IsNotEmpty()
  usedProcedures: number;

  @IsArray()
  @IsNotEmpty({ each: true })
  procedures: IProcedure[];

  constructor(name: string, procedure: IProcedure[], value: number, usedProcedures: number, isActive: boolean) {
    this.name = name;
    this.procedures = procedure;
    this.value = value;
    this.usedProcedures = usedProcedures;
    this.isActive = isActive;
  }
}
