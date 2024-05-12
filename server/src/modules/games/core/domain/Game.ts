import { SafeUser, UserId } from "../../../users/core/domain/User";
import { Card, getPlayersCards } from "./Cards";

export type GameId = number;

export enum EnvidoCall {
    ENVIDO = 'ENVIDO',
    REAL_ENVIDO = 'REAL_ENVIDO',
    FALTA_ENVIDO = 'FALTA_ENVIDO',
}

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
    cards: Record<SafeUser['id'], Card[]>;
    points: Record<SafeUser['id'], number>;
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
    cards: Record<SafeUser['id'], Card[]>;
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
    name: string;
    players: SafeUser[];
    state: GameState;
    events: GameEvent[];
};

export class Game {
    readonly id: GameId;
    readonly name: string;
    readonly players: SafeUser[];
    readonly state: GameState;
    readonly events: GameEvent[] = [];

    constructor(props: GameProps) {
        this.id = props.id;
        this.name = props.name;
        this.players = props.players;
        this.state = props.state;
        this.events = props.events;
    }

    static NEW_GAME_ID = 0;

    static new(user: SafeUser): Game {
        return new Game({
            id: this.NEW_GAME_ID,
            name: user.username,
            players: [user],
            events: [],
            state: {
                started: false,
                firstPlayer: user.id,
                cards: {},
                points: {},
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

    canJoin(userId: UserId): boolean {
        return !this.state.started && this.players.length === 1 && this.players[0].id !== userId;
    }

    join(user: SafeUser): Game {
        return this.copy({
            players: [...this.players, user],
        });
    }

    start(): Game {
        const [player1Cards, player2Cards] = getPlayersCards();
        const points = {
            [this.players[0].id]: 0,
            [this.players[1].id]: 0,
        }
        const cards = {
            [this.players[0].id]: player1Cards,
            [this.players[1].id]: player2Cards,
        }
        const events = [...this.events, this.buildStartEvent(), this.buildNextRoundEvent(points, cards)];

        return this.copy({
            events,
            state: {
                ...this.state,
                started: true,
                cards,
                points,
            },
        });
    }

    buildStartEvent(): GameEventStart {
        return {
            type: GameEventType.START,
        };
    }

    buildNextRoundEvent(points: Record<SafeUser['id'], number>, cards: Record<SafeUser['id'], Card[]>): GameEventNextRound {
        return {
            type: GameEventType.NEXT_ROUND,
            points,
            cards,
        };
    }
}
