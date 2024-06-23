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
        const game = this.games.get(id);
        if (game && !game.closed) {
            return Promise.resolve(game);
        }
        return Promise.resolve(undefined);
    }

    getByUserId(userId: number): Promise<Game | undefined> {
        return Promise.resolve(
            Array.from(this.games.values())
                .find(game => !game.closed && game.players.some(player => player.id === userId))
        );
    }

    getAll({available}: GetAllParams): Promise<Game[]> {
        return Promise.resolve(
            Array.from(this.games.values())
                .filter(game => available ? (!game.state.started && !game.closed) : true)
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

    deleteByUser(userId: number): Promise<Game> {
        const game = Array.from(this.games.values())
            .find(game => game.players.some(player => player.id === userId));
        if (!game) {
            throw new Error("User has no game");
        }
        this.games.delete(game.id);
        return Promise.resolve(game);
    }

    deleteNotStartedByUser(userId: number): Promise<Game | undefined> {
        const game = Array.from(this.games.values())
            .find(game => !game.state.started && game.players.some(player => player.id === userId));
        if (!game) {
            return Promise.resolve(undefined);
        }
        this.games.delete(game.id);
        return Promise.resolve(game);
    }
}