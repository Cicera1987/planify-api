import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersModule } from './modules/users/users.module';
import { ClientsModule } from './modules/clients/clients.module';

@Module({
  imports: [
    MongooseModule.forRoot(process.env.MONGODB_URI || 
      'mongodb+srv://ciceraribeiro:yTwSUXAxsJqu80Qu@planify.48zvn.mongodb.net/planify?retryWrites=true&w=majority',),
    UsersModule,
    ClientsModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
