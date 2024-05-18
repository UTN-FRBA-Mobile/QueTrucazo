import { UserRepository, userRepository } from "../../../users/core/domain/UserRepository";
import { GameNotifier, gameNotifier } from "../domain/GameNotifier";
import { GameRepository, gameRepository } from "../domain/GameRepository";

export class DeleteUserGame {
    constructor(private userRepository: UserRepository, private gameRepository: GameRepository, private gameNotifier: GameNotifier) { }

    public async invoke(username: string): Promise<void> {
        const user = await this.userRepository.getByUsername(username);
        if (!user) {
            throw new Error("User not found");
        }
        const game = await this.gameRepository.deleteByUser(user.id);
        this.gameNotifier.notifyGameCancelled(game.getPlayersIds());
    }
}

export const deleteUserGame = new DeleteUserGame(userRepository, gameRepository, gameNotifier);