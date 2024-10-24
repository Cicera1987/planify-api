import { Body, Controller, Get, Param, Post, Put } from "@nestjs/common";
import { ClientService } from "app/modules/clients/services/client.service";
import { Client } from "../entities/client.entity";
import { CreateClientDto } from "../dto/create.client.dto";

@Controller('clients')
export class ClientController{
    constructor(private readonly ClientService: ClientService) { }

    @Post()
    async create(@Body() createClientDto: CreateClientDto): Promise<Client> {
        return this.ClientService.create(createClientDto);
    }

    @Get()
    findAll(): Promise<Client[]> {
        return this.ClientService.findAll();
    }
    @Get(':id')
    findById(@Param('id') id: string): Promise<Client> {
        return this.ClientService.findById(id);
    }

    @Put(':id')
    update(
        @Param('id') id: string,
        @Body() updateData: Partial<Client>,
    ): Promise<Client> {
        return this.ClientService.update(id, updateData);
    }

}