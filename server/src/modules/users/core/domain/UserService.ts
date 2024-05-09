import { LoginError } from "./errors/LoginError";
import { UserAlreadyExists } from "./errors/UserAlreadyExists";
import { hashService, HashService } from "./HashService";
import { User } from "./User";
import { userRepository, UserRepository } from "./UserRepository";

export class UserService {
    constructor(private hashService: HashService, private userRepository: UserRepository) { }

    async create(user: User) {
        const existingUser = await this.userRepository.getByUsername(user.username);
        if (existingUser) {
            throw new UserAlreadyExists(user.username);
        }
        const hashedPassword = await this.hashService.hash(user.password);
        const hashedUser = user.copy({ password: hashedPassword });
        return this.userRepository.save(hashedUser);
    }

    async update(user: User) {
        const existingUser = await this.userRepository.getByUsername(user.username);
        if (existingUser && existingUser.id !== user.id) {
            throw new UserAlreadyExists(user.username);
        }
        return this.userRepository.save(user);
    }

    async login(username: string, password: string) {
        const user = await this.userRepository.getByUsername(username);

        if (!user)
            throw new LoginError();

        if (! await hashService.areEqual(password, user.password))
            throw new LoginError();

        return user.copy({ password: '' });
    }
}

export const userService = new UserService(hashService, userRepository);