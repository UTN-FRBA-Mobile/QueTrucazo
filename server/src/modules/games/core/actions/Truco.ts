import { UserId } from "../../../users/core/domain/User";
import { Game, TrucoCall } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class Truco {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(id: Game['id'], userId: UserId, call: TrucoCall): Promise<void> {
        const game = await this.gameService.getUserGame(id, userId);
        const updatedGame = game.truco(userId, call);
        const savedGame = await this.gameService.update(updatedGame);
        const newEvents = savedGame.getNewEvents(game);
        this.gameNotifier.notifyNewEvents(savedGame.getPlayersIds(), newEvents);
    }
}

export const truco = new Truco(gameService, gameNotifier);