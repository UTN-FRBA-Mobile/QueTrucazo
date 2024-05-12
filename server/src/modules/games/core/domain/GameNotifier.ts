import { ExpressApp } from "../../../../delivery/express/app";
import { UserId } from "../../../users/core/domain/User";
import { SocketGameNotifier } from "../../infrastructure/SocketGameNotifier";
import { Game, GameId } from "./Game";

export interface GameNotifier {
    joinGameRoom(userId: UserId, gameId: GameId): void;
    notifyJoinGame(userId: UserId, game: Game): void;
}

export const gameNotifier: GameNotifier = new SocketGameNotifier();