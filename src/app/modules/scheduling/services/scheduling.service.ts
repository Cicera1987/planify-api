import {
  BadRequestException,
  Injectable,
  NotFoundException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CreateSchedulingDto } from '../dtos/create-scheduling-dto';
import { IScheduling } from '../interfaces/scheduling.iterface';
import { FinalizeSchedulingDto } from '../dtos/finalize.dto';
import { ServicePackage } from 'app/modules/servicePackages/entities/service-package.entity';

@Injectable()
export class SchedulingService {
  constructor(
    @InjectModel('Scheduling')
    private readonly schedulingModel: Model<IScheduling>,
    @InjectModel('ServicePackage') // Adicione esta linha para injetar o modelo
    private readonly servicePackageModel: Model<ServicePackage>,
  ) { }

  async create(createSchedulingDto: CreateSchedulingDto): Promise<IScheduling> {
    const { date, time, client, procedure } = createSchedulingDto;

    if (!Array.isArray(client) || client.length === 0) {
      throw new TypeError('Client must be a non-empty array of IDs.');
    }

    if (!Array.isArray(procedure) || procedure.length === 0) {
      throw new TypeError('Procedure must be a non-empty array of IDs.');
    }

    const newScheduling = new this.schedulingModel({
      date,
      time,
      client,
      procedure,
      isCompleted: false,
    });
    return await newScheduling.save();
  }

  async findAll(): Promise<IScheduling[]> {
    return await this.schedulingModel
      .find({isCompleted: false})
      .populate('client')
      .populate('procedure', 'id name')
      .exec();
  }

  async findById(id: string): Promise<IScheduling> {
    const scheduling = await this.schedulingModel
      .findOne({ id }, {isCompleted: false})
      .populate('client')
      .populate('procedure', 'id name')
      .exec();

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
      .findOneAndUpdate({ id }, updateSchedulingDto, {
        new: true,
        runValidators: true,
      })
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


  async finalizeAppointment(id: string, finalizeDto: FinalizeSchedulingDto): Promise<{ message: string; packageStatus?: boolean }> {
    const scheduling = await this.schedulingModel
      .findById(id)
      .populate('client')
      .exec();

    if (!scheduling) {
      throw new NotFoundException('Agendamento não encontrado.');
    }

    if (scheduling.isCompleted) {
      throw new BadRequestException('Agendamento já foi finalizado.');
    }

    scheduling.isCompleted = true; // Marcar como finalizado
    scheduling.notes = finalizeDto.notes; // Adicionando notas
    await scheduling.save();

    // Verifica se existe um cliente
    const client = scheduling.client[0];
    if (!client) {
      throw new NotFoundException('Cliente não encontrado.');
    }

    // Verifica se o cliente possui pacote de serviço
    const clientPackageId = client.servicePackage[0];
    if (!clientPackageId) {
      return { message: 'Atendimento finalizado com sucesso e receita gerada.' };
    }

    const clientPackage = await this.servicePackageModel.findById(clientPackageId); // Certifique-se de que você tem um model para servicePackage
    if (!clientPackage) {
      throw new NotFoundException('Pacote de serviço não encontrado.');
    }

    clientPackage.usedProcedures++;

    if (clientPackage.usedProcedures >= clientPackage.procedures.length) {
      clientPackage.isActive = false;
      await clientPackage.save();

      return {
        message: 'Todos os procedimentos do pacote foram utilizados. O pacote está inativo. Ative-o novamente para liberar os procedimentos.',
        packageStatus: false,
      };
    }

    await clientPackage.save();
    return { message: 'Atendimento finalizado com sucesso.' }; 
  }


}
