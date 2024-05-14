import { SafeUser } from "../../../users/core/domain/User";
import { Game } from "./Game";
import { GameRepository, gameRepository } from "./GameRepository";
import { GameNotFound } from "./errors/GameNotFound";

export class GameService {
    constructor(private gameRepository: GameRepository) { }

    async createGame(u: SafeUser): Promise<Game> {
        if (await this.gameRepository.getByUserId(u.id)) {
            throw new Error('User already has a game');
        }

        return this.gameRepository.save(Game.new(u));
    }

    async joinGame(id: Game['id'], user: SafeUser): Promise<Game> {
        if (await this.gameRepository.getByUserId(user.id)) {
            throw new Error('User already has a game');
        }

        const game = await this.gameRepository.getById(id);
        if (!game) {
            throw new GameNotFound(id);
        }

        if (!game.canJoin(user.id)) {
            throw new Error('User cannot join game');
        }

        const updatedGame = game.join(user).start();

        return this.gameRepository.save(updatedGame);
    }

    async getUserGame(gameId: Game['id'], userId: SafeUser['id']): Promise<Game> {
        const game = await this.gameRepository.getById(gameId);
        if (!game) {
            throw new GameNotFound(gameId);
        }

        if (!game.hasUser(userId)) {
            throw new Error('User is not part of the game');
        }

        return game;
    }

    async update(game: Game): Promise<Game> {
        return this.gameRepository.save(game);
    }
}

export const gameService = new GameService(gameRepository);