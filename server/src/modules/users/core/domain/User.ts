export type UserId = number;
export type SafeUser = Omit<UserProps, 'password'> & { password: undefined };

export type UserProps = {
    id: UserId;
    email: string;
    password: string;
    isAdmin: boolean;
};

export class User {
    readonly id: UserId;
    readonly email: string;
    readonly password: string;
    readonly isAdmin: boolean;

    constructor(props?: UserProps) {
        if (props) {
            this.id = props.id;
            this.email = props.email;
            this.password = props.password;
            this.isAdmin = props.isAdmin;
        }
    }

    static NEW_USER_ID = 0;

    static new(props: Omit<UserProps, 'id' | 'isAdmin'>): User {
        return new User({
            ...props,
            isAdmin: false,
            id: this.NEW_USER_ID,
        });
    }

    copy(props: Partial<UserProps>): User {
        return new User({
            ...this,
            ...props,
        });
    }

    withoutPassword(): SafeUser {
        return { ...this, password: undefined };
    }
}
