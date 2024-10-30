import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { Scheduling, SchedulingSchema } from './entities/scheduling.entity';
import { SchedulingController } from './controllers/scheduling.controller';
import { SchedulingService } from './services/scheduling.service';
import {
  Procedure,
  ProcedureSchema,
} from '../procedures/entities/procedure.entity';
import { Client, ClientSchema } from '../clients/entities/client.entity';
import { ServicePackageModule } from '../servicePackages/service-package.module';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: Scheduling.name, schema: SchedulingSchema },
      { name: Client.name, schema: ClientSchema },
      { name: Procedure.name, schema: ProcedureSchema },

    ]),
    ServicePackageModule,
  ],
  controllers: [SchedulingController],
  providers: [SchedulingService],
})
export class SchedulingModule {}
