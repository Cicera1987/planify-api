import User  from '../models/userModel';

const createUser = async (name: string, email: string) => {
    const user = new User({ name, email });
    return await user.save();
};

const getAllUsers = async () => {
    return await User.find();
};

const updateUser = async (
    id: string,
    data: Partial<{
        name: string;
        email: string;
        specialty?: string;
        whatsapp?: string;
        isActive: boolean;
    }>
) => {
    const user = await User.findOneAndUpdate({ id }, data, { new: true });
    return user;
};



export { createUser, getAllUsers, updateUser };
