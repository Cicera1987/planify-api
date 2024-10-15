import { NextFunction, Request, Response } from "express";
import { createUser, getAllUsers, updateUser } from '../services/userService';

const createUserHandler = async (req: Request, res: Response) => {
    try {
        const { name, email } = req.body;
        const user = await createUser(name, email);
        res.status(201).json(user);
    } catch (error) {
        res.status(400).json({ message: 'Erro ao criar usuário', error });
    }
};


const getUsersHandler = async (_req: Request, res: Response) => {
    try {
        const users = await getAllUsers();
        res.status(200).json(users);
    } catch (error) {
        res.status(500).json({ message: 'Erro ao buscar usuários', error });
    }
};

 const updateUserHandler = async (
    req: Request,
    res: Response,
    next: NextFunction
): Promise<void> => {
    try {
        const { id } = req.params;
        const { name, email, specialty, whatsapp, isActive } = req.body;

        const user = await updateUser(id, {
            name,
            email,
            specialty,
            whatsapp,
            isActive,
        });

        if (!user) {
            res.status(404).json({ message: 'Usuário não encontrado' });
            return;
        }
        res.status(200).json(user);
    } catch (error) {
        next(error);
    }
};


export { createUserHandler, getUsersHandler, updateUserHandler };