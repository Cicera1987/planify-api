import {
  Controller,
  Post,
  Get,
  Param,
  Body,
  Patch,
  Delete,
} from '@nestjs/common';
import { IScheduling } from '../interfaces/scheduling.iterface';
import { CreateSchedulingDto } from '../dtos/create-scheduling-dto';
import { SchedulingService } from '../services/scheduling.service';

@Controller('scheduling')
export class SchedulingController {
  constructor(private readonly schedulingService: SchedulingService) {}

  @Post()
  async create(
    @Body() createSchedulingDto: CreateSchedulingDto,
  ): Promise<IScheduling> {
    return await this.schedulingService.create(createSchedulingDto);
  }

  @Get()
  async findAll(): Promise<IScheduling[]> {
    return await this.schedulingService.findAll();
  }

  @Get(':id')
  async findById(@Param('id') id: string): Promise<IScheduling> {
    return await this.schedulingService.findById(id);
  }

  @Patch(':id')
  async update(
    @Param('id') id: string,
    @Body() updateSchedulingDto: Partial<CreateSchedulingDto>,
  ): Promise<IScheduling> {
    return await this.schedulingService.update(id, updateSchedulingDto);
  }

  @Delete(':id')
  async remove(@Param('id') id: string): Promise<{ message: string }> {
    await this.schedulingService.remove(id);
    return { message: 'Scheduling successfully deleted' };
  }
}
