import { SafeUser } from "../../../users/core/domain/User";
import { Game } from "../domain/Game";
import { GameService, gameService } from "../domain/GameService";

export class JoinGame {
    constructor(private gameService: GameService) { }

    public async invoke(id: Game['id'], user: SafeUser): Promise<Game> {
        return this.gameService.joinGame(id, user);
    }
}

export const createGame = new JoinGame(gameService);