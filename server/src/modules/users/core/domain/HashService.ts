import { BcryptHashService } from "../../infrastructure/BcryptHashService";

export interface HashService {
  hash(value: string): Promise<string>;
  areEqual(value: string, hashedValue: string): Promise<boolean>;
}

export const hashService = new BcryptHashService();