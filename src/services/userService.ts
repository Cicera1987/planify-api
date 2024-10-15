import User from '../models/userModel';
import { IUser } from 'src/types/userTypes';


const createNewUser = async (name: string, email: string) => {
    const user = new User({ name, email });
    return await user.save();
};

const getAllUsers = async () => {
    return await User.find();
};

const updateUserById = async (
    id: string,
    data: Partial<IUser>
) => {
    const user = await User.findOneAndUpdate({ id }, data, { new: true });
    return user;
};

export { createNewUser, getAllUsers, updateUserById };

