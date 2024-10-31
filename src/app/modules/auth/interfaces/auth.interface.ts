export interface IAuth extends Document {
    id: string;
    username: string;
    email: string;
    password: string;
}