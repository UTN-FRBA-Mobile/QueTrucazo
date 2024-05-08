import { EntriesResult } from '../../../shared/domain/EntriesResult';
import { SafeUser } from '../domain/User';
import { userRepository, UserRepository } from '../domain/UserRepository';

export class GetAllUsers {
  constructor(private userRepository: UserRepository) { }

  async invoke(): Promise<EntriesResult<SafeUser>> {
    const result = await this.userRepository.getAll();

    return {
      ...result,
      entries: result.entries.map((user) => user.withoutPassword()),
    };
  }
}

export const getAllUsers = new GetAllUsers(userRepository);
