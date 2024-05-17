import { UserId } from "../../../users/core/domain/User";
import { Card } from "../domain/Cards";
import { Game } from "../domain/Game";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameService, gameService } from "../domain/GameService";

export class ThrowCard {
    constructor(private gameService: GameService, private gameNotifier: GameNotifier) { }

    public async invoke(id: Game['id'], userId: UserId, card: Card): Promise<void> {
        const game = await this.gameService.getUserGame(id, userId);
        const updatedGame = game.throwCard(userId, card);
        const savedGame = await this.gameService.update(updatedGame);
        const newEvents = savedGame.getNewEvents(game);
        this.gameNotifier.notifyNewEvents(savedGame.getPlayersIds(), newEvents);
    }
}

export const throwCard = new ThrowCard(gameService, gameNotifier);