import {
  Injectable,
  InternalServerErrorException,
  NotFoundException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import {
  Client,
  ClientDocument,
} from 'app/modules/clients/entities/client.entity';
import { iconUser } from 'assets/icons/iconUser';
import { Model } from 'mongoose';
import { CreateClientDto } from '../dtos/create.client.dto';

@Injectable()
export class ClientService {
  constructor(
    @InjectModel(Client.name) private clientModel: Model<ClientDocument>,
  ) {}

  async create(clientData: CreateClientDto): Promise<Client> {
    const { name, email, gender, phone, image, statusPackage, servicePackage } =
      clientData;

    if (!Array.isArray(servicePackage)) {
      throw new TypeError('ServicePackage must be an array of IDs.');
    }

    const client = new this.clientModel({
      name,
      email,
      gender,
      phone,
      image,
      statusPackage,
      servicePackage,
    });
    const savedClient = await client.save();
    return this.ensureImage(savedClient);
  }

  async findAll(): Promise<Client[]> {
    try {
      const clients = await this.clientModel
        .find()
        .populate('servicePackage')
        .exec();
      return clients.map(this.ensureImage);
    } catch (error) {
      console.error('Error fetching clients:', error);
      throw new InternalServerErrorException(
        'An error occurred while fetching clients.',
      );
    }
  }

  async findById(id: string): Promise<Client> {
    try {
      const client = await this.clientModel
        .findOne({ id })
        .populate('servicePackage')
        .exec();
      if (!client) {
        throw new NotFoundException('Client not found');
      }
      return this.ensureImage(client);
    } catch (error) {
      console.error('Error finding client by ID:', error);
      throw new InternalServerErrorException(
        'An error occurred while finding the client.',
      );
    }
  }

  async update(id: string, updateData: Partial<Client>): Promise<Client> {
    try {
      const updatedClient = await this.clientModel
        .findOneAndUpdate({ id }, updateData, {
          new: true,
          runValidators: true,
        })
        .exec();
      if (!updatedClient) {
        throw new NotFoundException('Client not found');
      }
      return this.ensureImage(updatedClient);
    } catch (error) {
      console.error('Error updating client:', error);
      throw new InternalServerErrorException(
        'An error occurred while updating the client.',
      );
    }
  }

  private ensureImage(client: ClientDocument): Client {
    return {
      ...client.toObject(),
      image: client.image || iconUser,
      statusPackage: client.statusPackage,
    };
  }
}
