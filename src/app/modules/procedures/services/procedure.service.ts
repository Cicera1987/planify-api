import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Procedure, ProcedureDocument } from '../entities/procedure.entity';

@Injectable()
export class ProcedureService {
    constructor(@InjectModel(Procedure.name) private ProcedureModel: Model<ProcedureDocument>) { }


    async create(ProcedureData: Partial<Procedure>): Promise<Procedure> {
        const savedProcedure = await this.ProcedureModel.create(ProcedureData);
        return savedProcedure; 
    }

    async findAll(): Promise<Procedure[]> {
        const procedures = await this.ProcedureModel.find().exec();
        return procedures
        
    }

    async findById(id: string): Promise<Procedure> {
        const procedure = await this.ProcedureModel.findById(id).exec();
        if (!procedure) throw new NotFoundException('User not found');
        return procedure

    }

    async update(id: string, updateData: Partial<Procedure>): Promise<Procedure> {
        const updatedProcedure = await this.ProcedureModel
            .findOneAndUpdate({ id }, updateData, { new: true, runValidators: true })
            .exec();
        if (!updatedProcedure) {
            throw new NotFoundException('User not found');
        }
        return updatedProcedure
    }

}
