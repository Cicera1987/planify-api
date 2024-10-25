import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument } from '../entities/user.entity';
import { iconUser } from '../../../../assets/icons/iconUser';

@Injectable()
export class UserService {
  constructor(@InjectModel(User.name) private userModel: Model<UserDocument>) {}

  async create(userData: Partial<User>): Promise<User> {
    const savedUser = await this.userModel.create(userData);
    return this.ensureImage(savedUser);
  }

  async findAll(): Promise<User[]> {
    const users = await this.userModel.find().exec();
    return users.map(this.ensureImage);
  }

  async findById(id: string): Promise<User> {
    const user = await this.userModel.findById(id).exec();
    if (!user) throw new NotFoundException('User not found');
    return this.ensureImage(user);
  }

  async update(id: string, updateData: Partial<User>): Promise<User> {
    const updatedUser = await this.userModel
      .findOneAndUpdate({ id }, updateData, { new: true, runValidators: true })
      .exec();
    if (!updatedUser) {
      throw new NotFoundException('User not found');
    }
    return this.ensureImage(updatedUser);
  }

  private ensureImage(user: UserDocument): User {
    return {
      ...user.toObject(),
      image: user.image || iconUser,
    };
  }
}
