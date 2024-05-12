import { EntriesResult } from "../../shared/domain/EntriesResult";
import { Game } from "../core/domain/Game";
import { GameRepository, GetAllParams } from "../core/domain/GameRepository";
import { GameNotFound } from "../core/domain/errors/GameNotFound";

export class InMemoryGameRepository implements GameRepository {

    nextId = 1;

    games: Map<Game['id'], Game> = new Map();

    clear() {
        this.games.clear();
        this.nextId = 1;
    }

    save(game: Game): Promise<Game> {
        if (game.id === Game.NEW_GAME_ID) {
            game = game.copy({ id: this.nextId++ });
        }
        this.games.set(game.id, game);
        return Promise.resolve(game);
    }

    getById(id: number): Promise<Game | undefined> {
        return Promise.resolve(this.games.get(id));
    }

    getByUserId(userId: number): Promise<Game | undefined> {
        return Promise.resolve(
            Array.from(this.games.values())
                .find(game => game.players.some(player => player.id === userId))
        );
    }

    getAll({available}: GetAllParams): Promise<Game[]> {
        return Promise.resolve(
            Array.from(this.games.values())
                .filter(game => available ? !game.state.started : true)
        );
    }

    delete(id: number): Promise<Game> {
        const game = this.games.get(id);
        if (!game) {
            throw new GameNotFound(id);
        }
        this.games.delete(id);
        return Promise.resolve(game);
    }
}