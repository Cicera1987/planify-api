import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule } from '@nestjs/config';
import { UsersModule } from './modules/users/users.module';
import config from 'app/config/config';

@Module({
  imports: [
    ConfigModule.forRoot({
      load: [config],
    }),
    MongooseModule.forRoot(config().mongodbURI),
    UsersModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}