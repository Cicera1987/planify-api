import { IsArray, IsNotEmpty, IsString } from 'class-validator';

export class FinalizeSchedulingDto {
    @IsArray()
    @IsNotEmpty({ message: 'A lista de procedureId não pode estar vazia.' })
    procedureId: string[];

    @IsString()
    @IsNotEmpty({ message: 'As notas não podem estar vazias.' })
    notes: string;

    constructor(procedureId: string[], notes: string) {
        this.procedureId = procedureId;
        this.notes = notes;
    }
    
}
