import { SafeUser, User } from '../domain/User';
import { userRepository, UserRepository } from '../domain/UserRepository';
import { UserNotFound } from '../domain/errors/UserNotFound';

export class GetUserById {
  constructor(private userRepository: UserRepository) { }

  async invoke(id: User['id']): Promise<SafeUser> {
    const user = await this.userRepository.getById(id);
    if (user === undefined) {
      throw new UserNotFound(id);
    }
    return user;
  }
}

export const getUserById = new GetUserById(userRepository);
