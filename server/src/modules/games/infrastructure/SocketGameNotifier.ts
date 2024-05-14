import { ExpressApp } from "../../../delivery/express/app";
import { UserId } from "../../users/core/domain/User";
import { Game, GameEvent, GameId } from "../core/domain/Game";
import { GameNotifier } from "../core/domain/GameNotifier";

export class SocketGameNotifier implements GameNotifier {
    notifyCreatedGame(userId: UserId, gameId: GameId) {
        ExpressApp.getInstance().emitToUser(userId, 'created-game', { gameId });
    }

    notifyJoinGame(userIds: UserId[], game: Game) {
        ExpressApp.getInstance().emitToUsers(userIds, 'join-game', game);
    }

    notifyNewEvents(userIds: UserId[], events: GameEvent[]) {
        ExpressApp.getInstance().emitToUsers(userIds, 'new-events', events);
    }

    notifyGameCancelled(userIds: UserId[]) {
        ExpressApp.getInstance().emitToUsers(userIds, 'game-cancelled', {});
    }
}