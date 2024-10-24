import { Injectable, NotFoundException } from "@nestjs/common";
import { InjectModel } from "@nestjs/mongoose";
import { Client, ClientDocument } from "app/modules/clients/entities/client.entity";
import { iconUser } from "assets/icons/iconUser";
import { Model } from "mongoose";


@Injectable()
export class ClientService{
    constructor(@InjectModel(Client.name) private clientModel: Model<ClientDocument>){}

    async create(ClientData: Partial<Client>): Promise<Client> {
        const savedUser = await this.clientModel.create(ClientData);
        return this.ensureImage(savedUser);
    }

    async findAll(): Promise<Client[]> {
        const clients = await this.clientModel.find().exec();
        return clients.map(this.ensureImage);
    }

    async findById(id: string): Promise<Client> {
        const user = await this.clientModel.findById(id).exec();
        if (!user) throw new NotFoundException('User not found');
        return this.ensureImage(user);
    }

    async update(id: string, updateData: Partial<Client>): Promise<Client> {
        const updatedClient = await this.clientModel
            .findOneAndUpdate({ id }, updateData, { new: true, runValidators: true })
            .exec();
        if (!updatedClient) {
            throw new NotFoundException('User not found');
        }
        return this.ensureImage(updatedClient);
    }

    private ensureImage(client: ClientDocument): Client {
        return {
            ...client.toObject(),
            image: client.image || iconUser,
        };
    }
}