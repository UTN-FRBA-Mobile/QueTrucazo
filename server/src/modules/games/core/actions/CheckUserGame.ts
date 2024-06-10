import { SafeUser } from "../../../users/core/domain/User";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameRepository, gameRepository } from "../domain/GameRepository";

export class CheckUserGame {
    constructor(private gameRepository: GameRepository, private gameNotifier: GameNotifier) { }

    public async invoke(userId: SafeUser['id']): Promise<void> {
        return this.gameRepository.getByUserId(userId).then((game) => {
            if (!game) return;
            this.gameNotifier.notifyJoinGame([userId], game);
        });
    }
}

export const checkUserGame = new CheckUserGame(gameRepository, gameNotifier);