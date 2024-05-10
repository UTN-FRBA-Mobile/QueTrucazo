import { SafeUser } from "../../../users/core/domain/User";
import { Game } from "./Game";
import { GameRepository, gameRepository } from "./GameRepository";
import { GameNotFound } from "./errors/GameNotFound";

export class GameService {
    constructor(private gameRepository: GameRepository) { }

    async createGame(u: SafeUser): Promise<Game> {
        return this.gameRepository.save(Game.new(u));
    }

    async joinGame(id: Game['id'], user: SafeUser): Promise<Game> {
        const game = await this.gameRepository.getById(id);
        if (!game) {
            throw new GameNotFound(id);
        }

        if (!game.canJoin(user)) {
            throw new Error('User cannot join game');
        }

        const updatedGame = game.join(user).start();

        return this.gameRepository.save(updatedGame);
    }
}

export const gameService = new GameService(gameRepository);