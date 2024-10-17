import { Controller, Get, Post, Param, Body, Put } from '@nestjs/common';
import { UserService } from '../services/user.service';
import { User } from '../entities/user.entity';
import { CreateUserDto } from '../dtos/create-user.dto';

@Controller('users')
export class UserController {
    constructor(private readonly userService: UserService) { }


    @Post()
    async create(@Body() createUserDto: CreateUserDto): Promise<User> {
        return this.userService.create(createUserDto);
    }

    @Get()
    findAll(): Promise<User[]> {
        return this.userService.findAll();
    }

    @Get(':id')
    findById(@Param('id') id: string): Promise<User> {
        return this.userService.findById(id);
    }

    @Put(':id')
    update(@Param('id') id: string, @Body() updateData: Partial<User>): Promise<User> {
        return this.userService.update(id, updateData);
    }
}
