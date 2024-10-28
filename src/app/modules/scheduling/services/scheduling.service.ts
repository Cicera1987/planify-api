import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateSchedulingDto } from '../dtos/create-scheduling-dto';
import { IScheduling } from '../interfaces/scheduling.iterface';


@Injectable()
export class SchedulingService {
    constructor(
        @InjectModel('Scheduling') private readonly schedulingModel: Model<IScheduling>,
    ) { }

    async create(createSchedulingDto: CreateSchedulingDto): Promise<IScheduling> {
        const newScheduling = new this.schedulingModel(createSchedulingDto);
        return await newScheduling.save();
    }

    async findAll(): Promise<IScheduling[]> {
        return await this.schedulingModel.find().exec();
    }

    async findById(id: string): Promise<IScheduling> {
        const scheduling = await this.schedulingModel.findOne({ id }).exec();
        if (!scheduling) {
            throw new NotFoundException('Scheduling not found');
        }
        return scheduling;
    }

    async update(
        id: string,
        updateSchedulingDto: Partial<CreateSchedulingDto>,
    ): Promise<IScheduling> {
        const updatedScheduling = await this.schedulingModel
            .findOneAndUpdate({ id }, updateSchedulingDto, { new: true, runValidators: true })
            .exec();
        if (!updatedScheduling) {
            throw new NotFoundException('Scheduling not found');
        }
        return updatedScheduling;
    }

    async remove(id: string): Promise<void> {
        const result = await this.schedulingModel.deleteOne({ id }).exec();
        if (result.deletedCount === 0) {
            throw new NotFoundException('Scheduling not found');
        }
    }
}
