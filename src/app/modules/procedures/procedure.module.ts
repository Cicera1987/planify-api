import { MongooseModule } from "@nestjs/mongoose";
import { Procedure, ProcedureSchema } from "./entities/procedure.entity";
import { ProcedureController } from "./controllers/procedure.controller";
import { ProcedureService } from "./services/procedure.service";
import { Module } from "@nestjs/common";

@Module({
    imports: [
        MongooseModule.forFeature([{ name: Procedure.name, schema: ProcedureSchema }]),
    ],
    controllers: [ProcedureController],
    providers: [ProcedureService],
})
export class ProceduresModule { }
