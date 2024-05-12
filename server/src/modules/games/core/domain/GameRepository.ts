import { UserId } from '../../../users/core/domain/User';
import { InMemoryGameRepository } from '../../infrastructure/InMemoryGameRepository';
import { Game, GameId } from './Game';

export type GetAllParams = {
    available?: boolean;
}

export interface GameRepository {
    save(game: Game): Promise<Game>;
    getById(id: GameId): Promise<Game | undefined>;
    getByUserId(userId: UserId): Promise<Game | undefined>;
    getAll(params: GetAllParams): Promise<Game[]>;
    delete(id: GameId): Promise<Game>;
}

export const gameRepository = new InMemoryGameRepository();
