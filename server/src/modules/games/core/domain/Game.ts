import { SafeUser } from "../../../users/core/domain/User";

export type GameId = number;

export enum EnvidoCall {
    ENVIDO = 'ENVIDO',
    REAL_ENVIDO = 'REAL_ENVIDO',
    FALTA_ENVIDO = 'FALTA_ENVIDO',
}

export type PlayerState = {
    score: number;
    cards: number[];
    thrownCards: number[];
};

export type Envido = {
    call: EnvidoCall;
    caller: SafeUser['id'];
    previousCalls: EnvidoCall[];
    winner: SafeUser['id'] | undefined;
    playerPoints: Record<SafeUser['id'], number> | undefined;
}

export enum TrucoCall {
    TRUCO = 'TRUCO',
    RETRUCO = 'RETRUCO',
    VALE_CUATRO = 'VALE_CUATRO',
}

export type Truco = {
    call: TrucoCall;
    caller: SafeUser['id'];
}

export type Step = {
    starter: SafeUser['id'];
    cards: Record<SafeUser['id'], string>;
    winner: SafeUser['id'] | undefined;
}

export type GameState = {
    started: boolean;
    firstPlayer: SafeUser['id'];
    players: Record<SafeUser['id'], PlayerState>;
    envido: Envido | undefined;
    truco: Truco | undefined;
    winner: SafeUser['id'] | undefined;
    round: number;
    step1: Step | undefined;
    step2: Step | undefined;
    step3: Step | undefined;
};

export type GameProps = {
    id: GameId;
    players: SafeUser[];
    gameState: GameState;
};

export class Game {
    readonly id: GameId;
    readonly players: SafeUser[];
    readonly gameState: GameState;

    constructor(props?: GameProps) {
        if (props) {
            this.id = props.id;
            this.players = props.players;
            this.gameState = props.gameState;
        }
    }

    static NEW_GAME_ID = 0;

    static new(user: SafeUser): Game {
        return new Game({
            id: this.NEW_GAME_ID,
            players: [user],
            gameState: {
                started: false,
                firstPlayer: user.id,
                players: {
                    [user.id]: {
                        score: 0,
                        cards: [],
                        thrownCards: [],
                    },
                },
                envido: undefined,
                truco: undefined,
                winner: undefined,
                round: 0,
                step1: undefined,
                step2: undefined,
                step3: undefined,
            }
        });
    }

    copy(props: Partial<GameProps>): Game {
        return new Game({
            ...this,
            ...props,
        });
    }

    canJoin(user: SafeUser): boolean {
        return !this.gameState.started && this.players.length === 1 && this.players[0].id !== user.id;
    }

    join(user: SafeUser): Game {
        return this.copy({
            players: [...this.players, user],
            gameState: {
                ...this.gameState,
                players: {
                    ...this.gameState.players,
                    [user.id]: {
                        score: 0,
                        cards: [],
                        thrownCards: [],
                    },
                },
            },
        });
    }
}
