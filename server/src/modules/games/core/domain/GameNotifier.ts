import { UserId } from "../../../users/core/domain/User";
import { SocketGameNotifier } from "../../infrastructure/SocketGameNotifier";
import { Game } from "./Game";

export interface GameNotifier {
    notifyJoinGame(userId: UserId, game: Game): void;
    notifyNewEvents(userIds: UserId[], events: any[]): void;
}

export const gameNotifier: GameNotifier = new SocketGameNotifier();