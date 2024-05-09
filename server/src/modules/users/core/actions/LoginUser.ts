import { SafeUser } from "../domain/User";
import { UserService, userService } from "../domain/UserService";

export class LoginUser {
  constructor(private userService: UserService) { }

  async invoke(username: string, password: string): Promise<SafeUser> {
    const user = await this.userService.login(username, password);
    return user.toSafeUser();
  }
}

export const loginUser = new LoginUser(userService);