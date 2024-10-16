import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersModule } from '../App/modules/users/users.module';
import { ConfigModule } from '@nestjs/config';
import configuration from './config/configuration';
@Module({
    imports: [
        ConfigModule.forRoot({
            load: [configuration],
        }),
        MongooseModule.forRoot(configuration().mongodbURI), 
        UsersModule,
    ],
})

export class AppModule { }
