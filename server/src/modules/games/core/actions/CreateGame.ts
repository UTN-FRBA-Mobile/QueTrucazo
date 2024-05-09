import { SafeUser } from "../../../users/core/domain/User";
import { Game } from "../domain/Game";
import { GameService, gameService } from "../domain/GameService";

export class CreateGame {
    constructor(private gameService: GameService) { }

    public async invoke(user: SafeUser): Promise<Game> {
        return this.gameService.createGame(user);
    }
}

export const createGame = new CreateGame(gameService);