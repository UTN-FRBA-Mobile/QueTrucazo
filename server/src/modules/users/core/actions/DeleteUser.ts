import { SafeUser, UserId } from '../domain/User';
import { userRepository, UserRepository } from '../domain/UserRepository';

export class DeleteUser {
  constructor(private userRepository: UserRepository) { }

  async invoke(id: UserId): Promise<SafeUser> {
    return (await this.userRepository.delete(id)).toSafeUser();
  }
}

export const deleteUser = new DeleteUser(userRepository);
