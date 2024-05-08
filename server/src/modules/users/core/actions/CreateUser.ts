import { SafeUser, User, UserProps } from '../domain/User';
import { userService, UserService } from '../domain/UserService';

export class CreateUser {
  constructor(private userService: UserService) { }

  async invoke(params: Omit<UserProps, 'id' | 'isAdmin'>): Promise<SafeUser> {
    const user = User.new(params);
    return (await this.userService.create(user)).withoutPassword();
  }
}

export const createUser = new CreateUser(userService);
