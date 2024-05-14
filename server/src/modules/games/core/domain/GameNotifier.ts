import { UserId } from "../../../users/core/domain/User";
import { SocketGameNotifier } from "../../infrastructure/SocketGameNotifier";
import { Game, GameId } from "./Game";

export interface GameNotifier {
    notifyCreatedGame(userId: UserId, gameId: GameId): void;
    notifyJoinGame(userIds: UserId[], game: Game): void;
    notifyNewEvents(userIds: UserId[], events: any[]): void;
    notifyGameCancelled(userIds: UserId[]): void;
}

export const gameNotifier: GameNotifier = new SocketGameNotifier();