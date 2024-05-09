import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";
import { SafeUser, User } from "../core/domain/User";

@Entity('users')
export class TypeormUser {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ unique: true })
    username: string;

    @Column()
    password: string;

    @Column({ type: 'boolean', default: false })
    isAdmin: boolean;

    static from(user: User): TypeormUser {
        const e = new TypeormUser();
        if (user.id != 0) e.id = user.id;
        e.username = user.username;
        e.password = user.password;
        e.isAdmin = user.isAdmin;
        return e;
    }

    toDomain(): User {
        return new User({
            id: this.id,
            username: this.username,
            password: this.password,
            isAdmin: this.isAdmin,
        });
    }
}
