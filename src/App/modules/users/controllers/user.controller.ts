import { Controller, Get, Post, Param, Body, Patch, Delete } from '@nestjs/common';
import { UserService } from '../services/user.service';
import { User } from '../entities/user.entity';
import { CreateUserDto } from '../dtos/create-user.dto';

@Controller('users')
export class UserController {
    constructor(private readonly userService: UserService) { }


    @Post()
    async create(@Body() createUserDto: CreateUserDto): Promise<User> {
        console.log('Novo usuário: ', CreateUserDto);
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

    @Patch(':id')
    update(@Param('id') id: string, @Body() updateData: Partial<User>): Promise<User> {
        return this.userService.update(id, updateData);
    }

    @Delete(':id')
    delete(@Param('id') id: string): Promise<void> {
        return this.userService.delete(id);
    }
}
