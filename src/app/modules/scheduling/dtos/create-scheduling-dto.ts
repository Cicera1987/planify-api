import { IClient } from "app/modules/clients/interfaces/client.interface";
import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";
import { IsArray, IsNotEmpty, IsString } from "class-validator";

export class CreateSchedulingDto {
  @IsString()
  @IsNotEmpty()
  date: Date;

  @IsString()
  @IsNotEmpty()
  time: string;

  @IsArray()
  @IsNotEmpty({ each: true })
  client: IClient[];

  @IsArray()
  @IsNotEmpty({ each: true })
  procedure: IProcedure[];

    constructor(
        date: Date,
        time: string,
        client: IClient[],
        procedure: IProcedure[],
    ) {
        this.date = date;
        this.time = time;
        this.client = client;
        this.procedure = procedure;
    }
}