import { IPackageMonthly } from 'app/modules/servicePackages/interfaces/service-package.interface';
import { IsArray, IsEmail, IsNotEmpty, IsOptional, ValidateIf, ValidateNested, } from 'class-validator';

export class CreateClientDto {
  @IsNotEmpty()
  name: string;

  @IsEmail()
  email?: string;

  @IsOptional()
  gender?: string;

  @IsOptional()
  phone?: string;

  @IsOptional()
  image?: string;

  @IsOptional()
  statusPackage?: boolean;

  @ValidateIf((dto: CreateClientDto) => dto.statusPackage === true)
  @IsArray()
  @ValidateNested({ each: true })
  package?: IPackageMonthly[];

  constructor(
    name: string,
    email: string,
    statusPackage: boolean,
    gender?: string,
    phone?: string,
    image?: string,
    packageData?: IPackageMonthly[],
  ) {
    this.name = name;
    this.email = email;
    this.statusPackage = statusPackage;
    this.gender = gender;
    this.phone = phone;
    this.image = image;
    this.package = packageData;
  }
}
