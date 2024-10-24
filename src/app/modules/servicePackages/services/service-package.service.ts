import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ServicePackage, ServicePackageDocument } from '../entities/service-package.entity'

@Injectable()
export class ServicePackageService {
    constructor(
        @InjectModel(ServicePackage.name) private servicePackageModel: Model<ServicePackageDocument>,
    ) { }

    async create(packageData: Partial<ServicePackage>): Promise<ServicePackage> {
        const createdPackage = await this.servicePackageModel.create(packageData);
        return createdPackage;
    }

    async findAll(): Promise<ServicePackage[]> {
        return this.servicePackageModel.find().populate('procedures').exec();
    }

    async findById(id: string): Promise<ServicePackage> {
        const servicePackage = await this.servicePackageModel.findById(id).populate('procedures').exec();
        if (!servicePackage) {
            throw new NotFoundException('Service package not found');
        }
        return servicePackage;
    }

    async update(id: string, updateData: Partial<ServicePackage>): Promise<ServicePackage> {
        const updatedPackage = await this.servicePackageModel
            .findByIdAndUpdate(id, updateData, { new: true, runValidators: true })
            .exec();
        if (!updatedPackage) {
            throw new NotFoundException('Service package not found');
        }
        return updatedPackage;
    }
}
