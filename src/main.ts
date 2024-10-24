import { NestFactory } from '@nestjs/core';
import { AppModule } from './app/app.module';
import { ValidationPipe } from '@nestjs/common';
import * as dotenv from 'dotenv';

dotenv.config();

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  const port = process.env.PORT || 3001;
  await app.listen(port);
  app.useGlobalPipes(new ValidationPipe());

  console.log(`Aplicação rodando na porta: http://localhost:${port}`);
}
bootstrap();
