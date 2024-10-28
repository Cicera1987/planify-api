import { IClient } from "app/modules/clients/interfaces/client.interface";
import { IProcedure } from "app/modules/procedures/interfaces/procedure.interface";

export interface IScheduling extends Document {
    id: string;
    date: Date;
    time: string
    client: IClient[];
    procedure: IProcedure[];
}
