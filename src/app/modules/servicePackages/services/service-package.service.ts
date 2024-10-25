import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model, Types } from 'mongoose';
import { ServicePackage, ServicePackageDocument } from '../entities/service-package.entity'
import { Procedure } from 'app/modules/procedures/entities/procedure.entity';
import { CreateServicePackageDto } from '../dtos/create.service-package';

@Injectable()
export class ServicePackageService {
    constructor(
        @InjectModel(ServicePackage.name) private packageModel: Model<ServicePackageDocument>,
        @InjectModel(Procedure.name) private procedureModel: Model<Procedure>,
    ) { }


    async create(createServicePackageDto: CreateServicePackageDto): Promise<ServicePackage> {
        const { name, value, procedures } = createServicePackageDto;

        // Verifique se procedures é realmente um array antes de iterar
        if (!Array.isArray(procedures)) {
            throw new TypeError('Procedures must be an array of IDs.');
        }

        const servicePackage = new this.packageModel({
            name,
            value,
            procedures
        });

        return servicePackage.save();
    }

    async findAll(): Promise<ServicePackage[]> {
        return this.packageModel.find().populate('procedures', 'id name').exec(); // Popula os dados de procedures com apenas `id` e `name`
    }

    async findOne(id: string): Promise<ServicePackage> {
        const servicePackage = await this.packageModel.findById(id).populate('procedures', 'id name').exec();
        if (!servicePackage) {
            throw new NotFoundException(`Package with ID ${id} not found.`);
        }
        return servicePackage;
    }

    async remove(id: string): Promise<void> {
        const result = await this.packageModel.findByIdAndDelete(id).exec();
        if (!result) {
            throw new NotFoundException(`Package with ID ${id} not found.`);
        }
    }

    async update(id: string, updatePackageDto: CreateServicePackageDto): Promise<ServicePackage> {
        const updatedPackage = await this.packageModel.findByIdAndUpdate(id, updatePackageDto, { new: true }).exec();
        if (!updatedPackage) {
            throw new NotFoundException(`Package with ID ${id} not found.`);
        }
        return updatedPackage;
    }


}
