import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ServicePackageController } from './controllers/service-package.controller';
import { ServicePackageService } from './services/service-package.service';
import { ServicePackage, ServicePackageSchema } from './entities/service-package.entity';



@Module({
    imports: [
        MongooseModule.forFeature([{ name: ServicePackage.name, schema: ServicePackageSchema }]),
    ],
    controllers: [ServicePackageController],
    providers: [ServicePackageService],
})
export class ServicePackageModule { }