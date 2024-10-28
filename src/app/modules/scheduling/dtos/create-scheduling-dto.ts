import { IClient } from "app/modules/clients/interfaces/client.interface";
import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";
import { IsNotEmpty, IsString } from "class-validator";

export class CreateSchedulingDto {
  @IsString()
  @IsNotEmpty()
  date: Date;

  @IsString()
  @IsNotEmpty()
  time: string;

  @IsString()
  @IsNotEmpty()
  client: IClient[];

  @IsString()
  @IsNotEmpty()
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