import { SafeUser } from "../../../users/core/domain/User";
import { Card, getPlayersCards } from "./Cards";

export type GameId = number;

export enum EnvidoCall {
    ENVIDO = 'ENVIDO',
    REAL_ENVIDO = 'REAL_ENVIDO',
    FALTA_ENVIDO = 'FALTA_ENVIDO',
}

export type PlayerState = {
    score: number;
    cards: Card[];
    thrownCards: Card[];
};

export type Envido = {
    call: EnvidoCall;
    caller: SafeUser['id'];
    previousCalls: EnvidoCall[];
    accepted: boolean | undefined;
    lastResponseBy: SafeUser['id'] | undefined;
    winner: SafeUser['id'] | undefined;
    playersPoints: Record<SafeUser['id'], number> | undefined;
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

export enum GameEventType {
    START = 'START',
    ENVIDO_CALL = 'ENVIDO_CALL',
    ENVIDO_ACCEPT = 'ENVIDO_ACCEPT',
    ENVIDO_DECLINE = 'ENVIDO_DECLINE',
    ENVIDO_RESULT = 'ENVIDO_RESULT',
    TRUCO_CALL = 'TRUCO_CALL',
    TRUCO_ACCEPT = 'TRUCO_ACCEPT',
    TRUCO_DECLINE = 'TRUCO_DECLINE',
    THROW_CARD = 'THROW_CARD',
    NEXT_ROUND = 'NEXT_ROUND',
}

export type GameEventStart = {
    type: GameEventType.START;
};

export type GameEventEnvidoCall = {
    type: GameEventType.ENVIDO_CALL;
    previousCalls: EnvidoCall[];
    call: EnvidoCall;
    caller: SafeUser['id'];
};

export type GameEventEnvidoAccept = {
    type: GameEventType.ENVIDO_ACCEPT;
    acceptedBy: SafeUser['id'];
};

export type GameEventEnvidoDecline = {
    type: GameEventType.ENVIDO_DECLINE;
    declinedBy: SafeUser['id'];
};

export enum EnvidoWinnerReason {
    MAS_PUNTOS = 'MAS_PUNTOS',
    ES_MANO = 'ES_MANO',
}

export type GameEventEnvidoResult = {
    type: GameEventType.ENVIDO_RESULT;
    winner: SafeUser['id'];
    playersPoints: Record<SafeUser['id'], number>;
    reason: EnvidoWinnerReason;
};

export type GameEventTrucoCall = {
    type: GameEventType.TRUCO_CALL;
    call: TrucoCall;
    caller: SafeUser['id'];
};

export type GameEventTrucoAccept = {
    type: GameEventType.TRUCO_ACCEPT;
    acceptedBy: SafeUser['id'];
    call: TrucoCall;
};

export type GameEventTrucoDecline = {
    type: GameEventType.TRUCO_DECLINE;
    declinedBy: SafeUser['id'];
    call: TrucoCall;
};

export type GameEventThrowCard = {
    type: GameEventType.THROW_CARD;
    player: SafeUser['id'];
    card: Card;
    step: 1 | 2 | 3;
};

export type GameEventNextRound = {
    type: GameEventType.NEXT_ROUND;
    points: Record<SafeUser['id'], number>;
};

export type GameEvent = GameEventStart 
    | GameEventEnvidoCall
    | GameEventEnvidoAccept
    | GameEventEnvidoDecline
    | GameEventEnvidoResult
    | GameEventTrucoCall
    | GameEventTrucoAccept
    | GameEventTrucoDecline
    | GameEventThrowCard
    | GameEventNextRound;

export type GameProps = {
    id: GameId;
    players: SafeUser[];
    state: GameState;
    events: GameEvent[];
};

export class Game {
    readonly id: GameId;
    readonly players: SafeUser[];
    readonly state: GameState;
    readonly events: GameEvent[] = [];

    constructor(props?: GameProps) {
        if (props) {
            this.id = props.id;
            this.players = props.players;
            this.state = props.state;
            this.events = props.events;
        }
    }

    static NEW_GAME_ID = 0;

    static new(user: SafeUser): Game {
        return new Game({
            id: this.NEW_GAME_ID,
            players: [user],
            events: [],
            state: {
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
        return !this.state.started && this.players.length === 1 && this.players[0].id !== user.id;
    }

    join(user: SafeUser): Game {
        return this.copy({
            players: [...this.players, user],
            state: {
                ...this.state,
                players: {
                    ...this.state.players,
                    [user.id]: {
                        score: 0,
                        cards: [],
                        thrownCards: [],
                    },
                },
            },
        });
    }

    start(): Game {
        const [player1Cards, player2Cards] = getPlayersCards();
        return this.copy({
            state: {
                ...this.state,
                started: true,
                players: {
                    ...this.state.players,
                    [this.players[0].id]: {
                        ...this.state.players[this.players[0].id],
                        cards: player1Cards,
                    },
                    [this.players[1].id]: {
                        ...this.state.players[this.players[1].id],
                        cards: player2Cards,
                    },
                },
            },
        });
    }
}
