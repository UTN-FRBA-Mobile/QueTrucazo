import { UserId } from "../../../users/core/domain/User";
import { Game } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class AnswerEnvido {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(id: Game['id'], userId: UserId, accepted: boolean): Promise<void> {
        const game = await this.gameService.getUserGame(id, userId);
        const updatedGame = game.answerEnvido(userId, accepted);
        const savedGame = await this.gameService.update(updatedGame);
        const newEvents = savedGame.getNewEvents(game);
        this.gameNotifier.notifyNewEvents(savedGame.getPlayersIds(), newEvents);
    }
}

export const answerEnvido = new AnswerEnvido(gameService, gameNotifier);