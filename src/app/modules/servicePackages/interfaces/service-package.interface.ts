import { IProcedure } from 'app/modules/procedures/interfaces/procedure.interface';
import { Document } from 'mongoose';

export interface IPackageMonthly extends Document {
    id: string;
    name: string;
    procedure: IProcedure[];
}
