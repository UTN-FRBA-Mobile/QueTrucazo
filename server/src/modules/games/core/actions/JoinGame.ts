import { SafeUser } from "../../../users/core/domain/User";
import { Game } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class JoinGame {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(id: Game['id'], user: SafeUser): Promise<void> {
        return this.gameService.joinGame(id, user).then((game) => {
            this.gameNotifier.notifyJoinGame(game.getPlayersIds(), game);
        });
    }
}

export const joinGame = new JoinGame(gameService, gameNotifier);