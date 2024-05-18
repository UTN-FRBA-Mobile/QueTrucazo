import { UserId } from "../../../users/core/domain/User";
import { EnvidoCall, Game } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class Envido {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(id: Game['id'], userId: UserId, envidoCall: EnvidoCall): Promise<void> {
        const game = await this.gameService.getUserGame(id, userId);
        const updatedGame = game.envido(userId, envidoCall);
        const savedGame = await this.gameService.update(updatedGame);
        const newEvents = savedGame.getNewEvents(game);
        this.gameNotifier.notifyNewEvents(savedGame.getPlayersIds(), newEvents);
    }
}

export const envido = new Envido(gameService, gameNotifier);