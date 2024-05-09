import { removeUndefined } from "../../../shared/domain/utils";
import { SafeUser, UserId, UserProps } from "../domain/User";
import { userRepository, UserRepository } from "../domain/UserRepository";
import { userService, UserService } from "../domain/UserService";
import { UserNotFound } from "../domain/errors/UserNotFound";

export class UpdateUser {
    constructor(private userService: UserService, private repository: UserRepository) { }

    async invoke(id: UserId, params: Partial<Omit<UserProps, "id">>): Promise<SafeUser> {
        const user = await this.repository.getById(id);
        if (!user)
            throw new UserNotFound(id);

        const updatedUser = user.copy(removeUndefined(params));
        return (await this.userService.update(updatedUser)).toSafeUser();
    }
}

export const updateUser = new UpdateUser(userService, userRepository);