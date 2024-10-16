import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { UsersModule } from './App/modules/users/users.module';

import { ConfigModule } from '@nestjs/config';

@Module({
    imports: [
        ConfigModule.forRoot(),
        MongooseModule.forRoot(process.env.MONGODB_URI ?? 'mongodb://localhost:27017/planify'),
        UsersModule,
    ],
})
export class AppModule { }
