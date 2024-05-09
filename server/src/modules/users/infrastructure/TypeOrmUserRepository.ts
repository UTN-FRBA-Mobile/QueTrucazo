import { Repository } from 'typeorm';
import { EntriesResult } from '../../shared/domain/EntriesResult';
import { SafeUser, User } from '../core/domain/User';
import { UserRepository } from '../core/domain/UserRepository';
import { TypeormUser } from './TypeormUser';
import { ResourceNotFound } from '../../shared/domain/errors/ResourceNotFound';
import { Resource } from '../../shared/domain/errors/HandledError';

export class TypeormUserRepository implements UserRepository {
    constructor(
        private repository: Repository<TypeormUser>,
    ) { }

    async save(user: User): Promise<User> {
        const createdUser = await this.repository.save(TypeormUser.from(user));
        return createdUser.toDomain();
    }

    async getById(id: number): Promise<SafeUser | undefined> {
        const user = await this.repository.findOne({ where: { id } });
        return user ? user.toDomain().toSafeUser() : undefined;
    }

    async getByUsername(username: string): Promise<User | undefined> {
        const user = await this.repository.findOne({ where: { username } });
        return user ? user.toDomain() : undefined;
    }

    async getAll(): Promise<EntriesResult<User>> {
        const [users, count] = await this.repository.findAndCount();

        return ({
            entries: users.map(user => user.toDomain()),
            pagination: {
                page: 1,
                pageSize: count,
                total: count,
            },
        });
    }

    async delete(id: number): Promise<User> {
        const user = await this.repository.findOneBy({ id });
        if (!user) {
            throw new ResourceNotFound(Resource.USER, id);
        }
        return (await this.repository.remove(user)).toDomain();
    }
}
