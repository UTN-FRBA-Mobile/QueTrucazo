import { Entity, PrimaryGeneratedColumn, Column } from "typeorm";
import { User } from "../core/domain/User";

@Entity('users')
export class TypeormUser {
    @PrimaryGeneratedColumn()
    id: number;

    @Column({ unique: true })
    email: string;

    @Column()
    password: string;

    @Column({ type: 'boolean', default: false })
    isAdmin: boolean;

    static from(user: User): TypeormUser {
        const e = new TypeormUser();
        if (user.id != 0) e.id = user.id;
        e.email = user.email;
        e.password = user.password;
        e.isAdmin = user.isAdmin;
        return e;
    }

    toDomain(): User {
        return new User({
            id: this.id,
            email: this.email,
            password: this.password,
            isAdmin: this.isAdmin,
        });
    }
}
