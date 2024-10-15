import {Router} from 'express';
import { createUserHandler, getUsersHandler, updateUserHandler} from '../controllers/userController';


const router = Router();

router.post('/', createUserHandler);
router.get('/', getUsersHandler);
router.put('/:id', updateUserHandler); 

export default router;