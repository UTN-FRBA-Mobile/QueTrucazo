import { LoginError } from "./errors/LoginError";
import { UserAlreadyExists } from "./errors/UserAlreadyExists";
import { hashService, HashService } from "./HashService";
import { User } from "./User";
import { userRepository, UserRepository } from "./UserRepository";

export class UserService {
    constructor(private hashService: HashService, private userRepository: UserRepository) { }

    async create(user: User) {
        const existingUser = await this.userRepository.getByEmail(user.email);
        if (existingUser) {
            throw new UserAlreadyExists(user.email);
        }
        const hashedPassword = await this.hashService.hash(user.password);
        const hashedUser = user.copy({ password: hashedPassword });
        return this.userRepository.save(hashedUser);
    }

    async update(user: User) {
        const existingUser = await this.userRepository.getByEmail(user.email);
        if (existingUser && existingUser.id !== user.id) {
            throw new UserAlreadyExists(user.email);
        }
        return this.userRepository.save(user);
    }

    async login(email: string, password: string) {
        const user = await this.userRepository.getByEmail(email);

        if (!user)
            throw new LoginError();

        if (! await hashService.areEqual(password, user.password))
            throw new LoginError();

        return user.copy({ password: '' });
    }
}

export const userService = new UserService(hashService, userRepository);