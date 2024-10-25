import { Injectable, InternalServerErrorException, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import {
  Client,
  ClientDocument,
} from 'app/modules/clients/entities/client.entity';
import { iconUser } from 'assets/icons/iconUser';
import { Model } from 'mongoose';

@Injectable()
export class ClientService {
  constructor(
    @InjectModel(Client.name) private clientModel: Model<ClientDocument>,
  ) { }

  async create(clientData: Partial<Client>): Promise<Client> {
    try {
      const savedClient = await this.clientModel.create(clientData);
      return this.ensureImage(savedClient);
    } catch (error) {
      console.error('Error creating client:', error);
      throw new InternalServerErrorException('An error occurred while creating the client.');
    }
  }

  async findAll(): Promise<Client[]> {
    try {
      const clients = await this.clientModel.find().exec();
      return clients.map(this.ensureImage);
    } catch (error) {
      console.error('Error fetching clients:', error);
      throw new InternalServerErrorException('An error occurred while fetching clients.');
    }
  }

  async findById(id: string): Promise<Client> {
    try {
      const client = await this.clientModel.findOne({ id }).exec(); // Busca pelo UUID
      if (!client) {
        throw new NotFoundException('Client not found');
      }
      return this.ensureImage(client);
    } catch (error) {
      console.error('Error finding client by ID:', error);
      throw new InternalServerErrorException('An error occurred while finding the client.');
    }
  }

  async update(id: string, updateData: Partial<Client>): Promise<Client> {
    try {
      const updatedClient = await this.clientModel
        .findOneAndUpdate({ id }, updateData, { new: true, runValidators: true })
        .exec();
      if (!updatedClient) {
        throw new NotFoundException('Client not found');
      }
      return this.ensureImage(updatedClient);
    } catch (error) {
      console.error('Error updating client:', error);
      throw new InternalServerErrorException('An error occurred while updating the client.');
    }
  }

  private ensureImage(client: ClientDocument): Client {
    return {
      ...client.toObject(),
      image: client.image || iconUser,
    };
  }
}