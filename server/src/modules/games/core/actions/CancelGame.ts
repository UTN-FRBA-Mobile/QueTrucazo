import { UserId } from "../../../users/core/domain/User";
import { GameId } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameRepository, gameRepository } from "../domain/GameRepository";
import { GameService, gameService } from "../domain/GameService";

export class CancelGame {
    constructor(private gameService: GameService, private gameRepository: GameRepository, private gameNotifier: GameNotifier) { }

    public async invoke(gameId: GameId, userId: UserId): Promise<void> {
        return this.gameService.getUserGame(gameId, userId).then((game) => {
            if (game.state.started)
                throw new Error('Game already started');
            
            this.gameRepository.delete(game.id);
            this.gameNotifier.notifyGameCancelled(game.getPlayersIds());
        });
    }
}

export const cancelGame = new CancelGame(gameService, gameRepository, gameNotifier);