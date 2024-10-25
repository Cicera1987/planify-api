import {
  Injectable,
  InternalServerErrorException,
  NotFoundException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import {
  ServicePackage,
  ServicePackageDocument,
} from '../entities/service-package.entity';
import { CreateServicePackageDto } from '../dtos/create.service-package';

@Injectable()
export class ServicePackageService {
  constructor(
    @InjectModel(ServicePackage.name)
    private packageModel: Model<ServicePackageDocument>,
  ) {}

  async create(
    createServicePackageDto: CreateServicePackageDto,
  ): Promise<ServicePackage> {
    const { name, value, procedures } = createServicePackageDto;

    if (!Array.isArray(procedures)) {
      throw new TypeError('Procedures must be an array of IDs.');
    }

    const servicePackage = new this.packageModel({
      name,
      value,
      procedures,
    });

    return servicePackage.save();
  }

  async findAll(): Promise<ServicePackage[]> {
    try {
      const servicePackages = await this.packageModel
        .find()
        .populate('procedures', 'id name')
        .exec();

      return servicePackages;
    } catch (error) {
      console.error('Error retrieving service packages:', error);
      throw new InternalServerErrorException(
        'An error occurred while retrieving service packages.',
      );
    }
  }

  async findOne(id: string): Promise<ServicePackage> {
    try {
      const servicePackage = await this.packageModel
        .findOne({ _id: id })
        .populate('procedures', 'id name')
        .exec();

      if (!servicePackage) {
        throw new NotFoundException(`Package with ID ${id} not found.`);
      }

      return servicePackage;
    } catch (error) {
      console.error('Error finding package:', error);
      if (error instanceof NotFoundException) {
        throw error;
      }
      throw new InternalServerErrorException(
        'An error occurred while finding the package.',
      );
    }
  }

  async update(
    id: string,
    updatePackageDto: CreateServicePackageDto,
  ): Promise<ServicePackage> {
    try {
      const updatedPackage = await this.packageModel
        .findOneAndUpdate({ _id: id }, updatePackageDto, { new: true })
        .exec();

      if (!updatedPackage) {
        throw new NotFoundException(`Package with ID ${id} not found.`);
      }

      return updatedPackage;
    } catch (error) {
      console.error('Error updating package:', error);
      throw new InternalServerErrorException(
        'An error occurred while updating the package.',
      );
    }
  }

  async remove(id: string): Promise<void> {
    try {
      const result = await this.packageModel.findOneAndDelete({ id }).exec();

      if (!result) {
        throw new NotFoundException(`Package with ID ${id} not found.`);
      }
    } catch (error) {
      console.error('Error removing package:', error);
      throw new InternalServerErrorException(
        'An error occurred while removing the package.',
      );
    }
  }
}
