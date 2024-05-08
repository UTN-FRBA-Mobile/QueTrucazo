import { HashService } from "../core/domain/HashService";
import bcrypt from "bcrypt";

export class BcryptHashService implements HashService {
  public async hash(value: string): Promise<string> {
    const salt = await bcrypt.genSalt(6);
    return bcrypt.hash(value, salt);
  }

  public async areEqual(value: string, hashedValue: string): Promise<boolean> {
    return bcrypt.compare(value, hashedValue);
  }
}