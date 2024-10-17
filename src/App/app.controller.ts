import { Controller, Get } from "@nestjs/common";
import { UserController } from "./modules/users/controllers/user.controller";



@Controller()

export class AppController {
    constructor(private readonly userController: UserController) { }

    @Get()
    getHello(): string {
        return  "Api Planify"
    }
}