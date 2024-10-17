import { Test, TestingModule } from "@nestjs/testing";
import { UserService } from "./services/user.service";
import { getModelToken } from "@nestjs/mongoose"; // Importando getModelToken para usar no mock
import { User } from "../users/entities/user.entity"

describe('UserService', () => {
    let userService: UserService;

    const mockUserModel = {
        find: jest.fn().mockReturnThis(),
        exec: jest.fn().mockResolvedValue([]),
    };


    // init module before each test
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

});