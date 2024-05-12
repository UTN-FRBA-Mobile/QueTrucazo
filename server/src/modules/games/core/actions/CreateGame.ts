import { SafeUser } from "../../../users/core/domain/User";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class CreateGame {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(user: SafeUser): Promise<void> {
        return this.gameService.createGame(user).then((game) => {
            this.gameNotifier.notifyJoinGame(user.id, game);
        });
    }
}

export const createGame = new CreateGame(gameService, gameNotifier);