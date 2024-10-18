import { Test, TestingModule } from "@nestjs/testing";
import { UserService } from "./services/user.service";
import { getModelToken } from "@nestjs/mongoose";
import { User } from "./entities/user.entity";

describe('UserService', () => {
    let userService: UserService;

    const mockUser = {
        name: 'John Doe',
        email: 'john@example.com',
        specialty: 'developer',
        phone: '123456789',
        image: 'image.jpg',
    };

    const mockUserModel = {
        create: jest.fn().mockImplementation(async (userData) => {
            return {
                ...userData,
                toObject: jest.fn().mockReturnValue(userData),
            };
        }),
        find: jest.fn().mockReturnThis(),
        exec: jest.fn().mockResolvedValue([]),
    };


    beforeEach(async () => {
        const module: TestingModule = await Test.createTestingModule({
            providers: [
                UserService,
                {
                    provide: getModelToken(User.name),
                    useValue: mockUserModel,
                },
            ],
        }).compile();

        userService = module.get<UserService>(UserService);
    });

    describe('findAll', () => {
        it('should return an array of users', async () => {
            const result = await userService.findAll();
            expect(result).toEqual([]);
        });
    });

    // Testing the user creation method
    describe('create', () => {
        it('should create a user', async () => {
            const result = await userService.create(mockUser);
            expect(result).toEqual(mockUser);
            expect(mockUserModel.create).toHaveBeenCalledWith(mockUser);
        });
    });

    // Testing the user update method
    


});
