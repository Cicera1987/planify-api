import {
  ConflictException,
  Injectable,
  InternalServerErrorException,
  NotFoundException,
} from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { User, UserDocument } from '../entities/user.entity';
import { iconUser } from '../../../../assets/icons/iconUser';
import * as bcrypt from 'bcrypt';
import { CreateUserDto } from '../dtos/create-user.dto';
import { IRegister } from '../interfaces/user.interface';

@Injectable()
export class UserService {
  constructor(@InjectModel(User.name) private userModel: Model<UserDocument>) {}

  async create(userData: Partial<User>): Promise<User> {
    const newUser = new this.userModel(userData);
    try {
      const savedUser = await newUser.save();
      return this.ensureImage(savedUser) as User;
    } catch (error) {
      console.error('Error creating user:', error);
      throw new InternalServerErrorException(
        'An error occurred while creating the user.',
      );
    }
  }

  async registerUser(registerDto: CreateUserDto): Promise<User> {
    const existingUser = await this.findByEmail(registerDto.email);
    if (existingUser) {
      throw new ConflictException('Email already in use');
    }

    const hashedPassword = await bcrypt.hash(registerDto.password, 10);
    const newUser: IRegister = {
      name: registerDto.name,
      email: registerDto.email,
      password: hashedPassword,
      isActive: true,
      isAdmin: false,
    };

    const createdUser = new this.userModel(newUser);
    try {
      const savedUser = await createdUser.save();
      return this.ensureImage(savedUser) as User;
    } catch (error) {
      console.error('Error creating user:', error);
      throw new InternalServerErrorException('An error occurred while creating the user.');
    }
  }



  async findAll(): Promise<User[]> {
    try {
      const users = await this.userModel.find().exec();
      return users.map(this.ensureImage);
    } catch (error) {
      console.error('Error fetching users:', error);
      throw new InternalServerErrorException(
        'An error occurred while fetching users.',
      );
    }
  }

  async findById(id: string): Promise<User> {
    try {
      const user = await this.userModel.findOne({ id }).exec();
      if (!user) {
        throw new NotFoundException('User not found');
      }
      return this.ensureImage(user);
    } catch (error) {
      console.error('Error finding user by ID:', error);
      throw new InternalServerErrorException(
        'An error occurred while finding the user.',
      );
    }
  }
 
  async findByEmail(email: string): Promise<User | null> {
    const user = await this.userModel.findOne({ email }).exec();
    return user ? this.ensureImage(user) : null;
  }


  async update(id: string, updateData: Partial<User>): Promise<User> {
    try {
      const updatedUser = await this.userModel
        .findOneAndUpdate({ id }, updateData, {
          new: true,
          runValidators: true,
        })
        .exec();
      if (!updatedUser) {
        throw new NotFoundException('User not found');
      }
      return this.ensureImage(updatedUser);
    } catch (error) {
      console.error('Error updating user:', error);
      throw new InternalServerErrorException(
        'An error occurred while updating the user.',
      );
    }
  }

  private ensureImage(user: UserDocument & { isAdmin?: boolean }): User {
    return {
      ...user.toObject(),
      image: user.image || iconUser,
      isAdmin: user.isAdmin || false,
    };
  }

  
}
