import { SafeUser } from "../domain/User";
import { UserService, userService } from "../domain/UserService";

export class LoginUser {
  constructor(private userService: UserService) { }

  async invoke(email: string, password: string): Promise<SafeUser> {
    const user = await this.userService.login(email, password);
    return user.withoutPassword();
  }
}

export const loginUser = new LoginUser(userService);