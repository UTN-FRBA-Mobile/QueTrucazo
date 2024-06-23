import { UserId } from "../../../users/core/domain/User";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameRepository, gameRepository } from "../domain/GameRepository";

export class DeleteWaitingUserGame {
    constructor(private gameRepository: GameRepository, private gameNotifier: GameNotifier) { }

    public async invoke(userId: UserId): Promise<void> {
        const game = await this.gameRepository.deleteNotStartedByUser(userId);
        if (game === undefined) {
            return;
        }
        this.gameNotifier.notifyGameCancelled(game.getPlayersIds());
    }
}

export const deleteWaitingUserGame = new DeleteWaitingUserGame(gameRepository, gameNotifier);