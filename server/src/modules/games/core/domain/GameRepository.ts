import { EntriesResult } from '../../../shared/domain/EntriesResult';
import { InMemoryGameRepository } from '../../infrastructure/InMemoryGameRepository';
import { Game, GameId } from './Game';

export interface GameRepository {
    save(game: Game): Promise<Game>;
    getById(id: GameId): Promise<Game | undefined>;
    getAll(): Promise<EntriesResult<Game>>;
    delete(id: GameId): Promise<Game>;
}

export const gameRepository = new InMemoryGameRepository();
