import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument } from '../entities/user.entity';

@Injectable()
export class UserService {
    constructor(
        @InjectModel(User.name) private userModel: Model<UserDocument>,
    ) { }

    async create(userData: Partial<User>): Promise<User> {
        const newUser = new this.userModel(userData);
        return newUser.save();
    }

    async findAll(): Promise<User[]> {
        return this.userModel.find().exec();
    }

    async findById(id: string): Promise<User> {
        const user = await this.userModel.findById(id).exec();
        if (!user) throw new NotFoundException('User not found');
        return user;
    }

    async update(id: string, updateData: Partial<User>): Promise<User> {
        const updatedUser = await this.userModel.findOneAndUpdate(
            { id }, 
            updateData,
            { new: true, runValidators: true }
        ).exec();
        if (!updatedUser) {
            throw new NotFoundException('User not found');
        }

        return updatedUser;
    }
}
