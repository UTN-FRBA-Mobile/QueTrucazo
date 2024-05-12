import { ExpressApp } from "../../../delivery/express/app";
import { UserId } from "../../users/core/domain/User";
import { Game, GameId } from "../core/domain/Game";
import { GameNotifier } from "../core/domain/GameNotifier";

export class SocketGameNotifier implements GameNotifier {

    joinGameRoom(userId: UserId, gameId: GameId) {
        ExpressApp.getInstance().join(`game-${gameId}`, userId);
    }

    notifyJoinGame(userId: UserId, game: Game) {
        ExpressApp.getInstance().emitToUser(userId, 'join-game', { game });
    }
}