import { Game } from "../domain/Game";
import { GameRepository, gameRepository } from "../domain/GameRepository";

export class GetAllAvailableGames {
    constructor(private gameRepository: GameRepository) { }

    public async invoke(): Promise<Game[]> {
        return this.gameRepository.getAll({ available: true });
    }
}

export const getAllAvailableGames = new GetAllAvailableGames(gameRepository);