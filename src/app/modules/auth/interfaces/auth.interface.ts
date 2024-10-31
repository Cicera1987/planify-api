export interface IAuth extends Document {
    id: string;
    username: string;
    email: string;
    password: string;
}

// logout.interface.ts
export interface ILogout extends Document {
    message: string;
}
