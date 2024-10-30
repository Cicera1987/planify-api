import { IPackageMonthly } from 'app/modules/servicePackages/interfaces/service-package.interface';
import { Document } from 'mongoose';

export interface IClient extends Document {
  id: string;
  name: string;
  email: string;
  gender?: string;
  phone?: string;
  image?: string;
  statusPackage: boolean;
  servicePackage: IPackageMonthly[];
}
