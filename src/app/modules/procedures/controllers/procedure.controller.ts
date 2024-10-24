import { Controller, Get, Post, Param, Body, Put } from '@nestjs/common';
import { CreateProcedureDto } from '../dtos/create-procedure.dto.ts';
import { Procedure } from '../entities/procedure.entity';
import { ProcedureService } from '../services/procedure.service.js';

@Controller('procedures')
export class ProcedureController {
    constructor(private readonly procedureService: ProcedureService) { }

    @Post()
    async create(@Body() createProcedureDto: CreateProcedureDto): Promise<Procedure> {
        return this.procedureService.create(createProcedureDto);
    }

    @Get()
    findAll(): Promise<Procedure[]> {
        return this.procedureService.findAll();
    }

    @Get(':id')
    findById(@Param('id') id: string): Promise<Procedure> {
        return this.procedureService.findById(id);
    }

    @Put(':id')
    update(
        @Param('id') id: string,
        @Body() updateData: Partial<Procedure>,
    ): Promise<Procedure> {
        return this.procedureService.update(id, updateData);
    }
}
