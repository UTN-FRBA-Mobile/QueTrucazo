import { SafeUser } from "../domain/User";
import { TokenEncoded, tokenService, TokenService } from "../domain/TokenService";

export class GetUserToken {
  constructor(private tokenService: TokenService) { }

  public invoke({ id, email }: SafeUser): TokenEncoded {
    return this.tokenService.encode({ id, email });
  }
}

export const getUserToken = new GetUserToken(tokenService);