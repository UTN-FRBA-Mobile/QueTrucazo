import { SafeUser } from "../domain/User";
import { TokenEncoded, tokenService, TokenService } from "../domain/TokenService";

export class GetUserToken {
  constructor(private tokenService: TokenService) { }

  public invoke({ id, username }: SafeUser): TokenEncoded {
    return this.tokenService.encode({ id, username });
  }
}

export const getUserToken = new GetUserToken(tokenService);