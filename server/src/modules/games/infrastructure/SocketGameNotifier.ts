import { ExpressApp } from "../../../delivery/express/app";
import { UserId } from "../../users/core/domain/User";
import { Game, GameEvent, GameId } from "../core/domain/Game";
import { GameNotifier } from "../core/domain/GameNotifier";

export class SocketGameNotifier implements GameNotifier {
    notifyJoinGame(userId: UserId, game: Game) {
        ExpressApp.getInstance().emitToUser(userId, 'join-game', { game });
    }

    notifyNewEvents(userIds: UserId[], events: GameEvent[]) {
        ExpressApp.getInstance().emitToUsers(userIds, 'new-events', { events });
    }
}