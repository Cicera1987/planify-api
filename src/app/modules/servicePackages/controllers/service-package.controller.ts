import { Controller, Get, Post, Param, Body, Put, NotFoundException } from '@nestjs/common';
import { ServicePackageService } from '../services/service-package.service';
import { ServicePackage } from '../entities/service-package.entity';

@Controller('service-packages')
export class ServicePackageController {
    constructor(private readonly servicePackageService: ServicePackageService) { }

    @Post()
    async create(@Body() packageData: Partial<ServicePackage>): Promise<ServicePackage> {
        return this.servicePackageService.create(packageData);
    }

    @Get()
    async findAll(): Promise<ServicePackage[]> {
        return this.servicePackageService.findAll();
    }

    @Get(':id')
    async findById(@Param('id') id: string): Promise<ServicePackage> {
        return this.servicePackageService.findById(id);
    }

    @Put(':id')
    async update(
        @Param('id') id: string,
        @Body() updateData: Partial<ServicePackage>,
    ): Promise<ServicePackage> {
        return this.servicePackageService.update(id, updateData);
    }
}
