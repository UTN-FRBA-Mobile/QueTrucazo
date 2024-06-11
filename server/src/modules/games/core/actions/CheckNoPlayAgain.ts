import { UserId } from "../../../users/core/domain/User";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameRepository, gameRepository } from "../domain/GameRepository";
import { GameService, gameService } from "../domain/GameService";

export class CheckNoPlayAgain {
    constructor(private gameService: GameService, private gameRepository: GameRepository, private gameNotifier: GameNotifier) { }

    public async invoke(userId: UserId): Promise<void> {
        const game = await this.gameRepository.getByUserId(userId);
        if (!game) {
            return;
        }
        const updatedGame = game.noPlayAgain(userId);
        const savedGame = await this.gameService.update(updatedGame);
        const newEvents = savedGame.getNewEvents(game);
        this.gameNotifier.notifyNewEvents(savedGame.getPlayersIds(), newEvents);
    }
}

export const checkNoPlayAgain = new CheckNoPlayAgain(gameService, gameRepository, gameNotifier);