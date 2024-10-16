import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { ValidationPipe } from '@nestjs/common';
import * as dotenv from 'dotenv';

// Carrega variáveis de ambiente do arquivo .env
dotenv.config();

async function bootstrap() {
    const app = await NestFactory.create(AppModule);

    // Aplicar o ValidationPipe globalmente
    app.useGlobalPipes(new ValidationPipe());

    // Definindo a porta a partir das variáveis de ambiente
    const port = process.env.PORT || 3000;
    await app.listen(port);
    console.log(`Application is running on: http://localhost:${port}`);
}

bootstrap();
