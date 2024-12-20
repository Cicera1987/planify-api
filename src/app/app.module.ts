import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersModule } from './modules/users/users.module';
import { ClientsModule } from './modules/clients/clients.module';
import { ProceduresModule } from './modules/procedures/procedure.module';
import { ServicePackageModule } from './modules/servicePackages/service-package.module';
import { AuthModule } from './modules/auth/auth.module';
import { ConfigModule } from '@nestjs/config';
import { PassportModule } from '@nestjs/passport';
import { SchedulingModule } from './modules/scheduling/scheduling.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    PassportModule.register({ defaultStrategy: 'google' }),
    MongooseModule.forRoot(
      process.env.MONGODB_URI ||
        'mongodb+srv://ciceraribeiro:yTwSUXAxsJqu80Qu>@<planify.48zvn.mongodb.net/planify?retryWrites=true&w=majority',
    ),
    UsersModule,
    ClientsModule,
    ProceduresModule,
    ServicePackageModule,
    AuthModule,
    SchedulingModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
