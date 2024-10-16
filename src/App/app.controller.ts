import { Controller, Get } from "@nestjs/common";
import { UserController } from "./modules/Users/controllers/user.controller";



@Controller()

export class AppController {
    constructor(private readonly userController: UserController) { }

    @Get()
    getHello(): string {
        return  "Api Planify"
    }
}