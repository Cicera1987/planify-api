import {
  Controller,
  Get,
  Post,
  Param,
  Body,
  Put,
  Delete,
} from '@nestjs/common';
import { ServicePackageService } from '../services/service-package.service';
import { CreateServicePackageDto } from '../dtos/create.service-package';

@Controller('service-packages')
export class ServicePackageController {
  constructor(private readonly servicePackageService: ServicePackageService) {}

  @Post()
  async create(@Body() createPackageDto: CreateServicePackageDto) {
    return this.servicePackageService.create(createPackageDto);
  }

  @Get()
  findAll() {
    return this.servicePackageService.findAll();
  }

  @Get(':id')
  findOne(@Param('id') id: string) {
    return this.servicePackageService.findOne(id);
  }

  @Delete(':id')
  remove(@Param('id') id: string) {
    return this.servicePackageService.remove(id);
  }

  @Put(':id')
  update(
    @Param('id') id: string,
    @Body() updatePackageDto: CreateServicePackageDto,
  ) {
    return this.servicePackageService.update(id, updatePackageDto);
  }
}
