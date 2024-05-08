import { EntriesResult } from '../../../shared/domain/EntriesResult';
import { TypeormConnectionManager } from '../../../shared/infrastructure/TypeormConnectionManager';
import { TypeormUser } from '../../infrastructure/TypeormUser';
import { TypeormUserRepository } from '../../infrastructure/TypeOrmUserRepository';
import { User, UserId } from './User';

export interface UserRepository {
    save(user: User): Promise<User>;
    getById(id: UserId): Promise<User | undefined>;
    getByEmail(email: string): Promise<User | undefined>;
    getAll(): Promise<EntriesResult<User>>;
    delete(id: UserId): Promise<User>;
}

export const userRepository = new TypeormUserRepository(TypeormConnectionManager.getConnection(TypeormUser));
