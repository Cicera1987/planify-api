import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { Client, ClientSchema } from './entities/client.entity';
import { ClientController } from './controllers/client.controller';
import { ClientService } from './services/client.service';
import {
  ServicePackage,
  ServicePackageSchema,
} from '../servicePackages/entities/service-package.entity';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Client.name, schema: ClientSchema },
      { name: ServicePackage.name, schema: ServicePackageSchema },
    ]),
  ],
  controllers: [ClientController],
  providers: [ClientService],
})
export class ClientsModule {}
