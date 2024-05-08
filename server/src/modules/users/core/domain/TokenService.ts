import { JwtTokenService } from "../../infrastructure/JwtTokenService";
import { User, UserId } from "./User";

export type PartialSession = Omit<Session, "issued" | "expires">;

export interface Session {
  id: UserId;
  email: string;
  issued: number;
  expires: number;
}

export interface TokenEncoded {
  token: string,
  expires: number,
  issued: number
}

export interface TokenDecoded {
  type: 'valid-token' | 'integrity-error' | 'invalid-token';
  session?: Session;
}

export interface TokenValidateResult {
  valid: boolean;
  tokenDecoded?: TokenDecoded;
  message?: string;
}

export interface TokenService {
  validate(token: string): TokenValidateResult;
  encode(partialSession: PartialSession): TokenEncoded;
  decode(token: string): TokenDecoded;
}

export const tokenService = new JwtTokenService('secret-key');