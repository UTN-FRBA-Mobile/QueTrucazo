import { SafeUser, UserId } from "../../../users/core/domain/User";
import { Card, generatePlayersCards, getCardValue, getRoundWinner } from "./Cards";

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

export type GameState = {
    started: boolean;
    firstPlayer: SafeUser['id'];
    playerTurn: SafeUser['id'];
    winner: SafeUser['id'] | undefined;
    round: number;
    cards: Record<SafeUser['id'], Card[]>;
    thrownCards: Record<SafeUser['id'], Card[]>;
    trucoPoints: number;
    points: UsersPoints;
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
    ROUND_RESULT = 'ROUND_RESULT',
    NEXT_ROUND = 'NEXT_ROUND',
    RESULT = 'RESULT',
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

export type GameStep = 1 | 2 | 3;

export type GameEventThrowCard = {
    type: GameEventType.THROW_CARD;
    player: SafeUser['id'];
    card: Card;
    step: GameStep;
};

export type UsersPoints = Record<SafeUser['id'], number>;

export type GameEventRoundResult = {
    type: GameEventType.ROUND_RESULT;
    winner: SafeUser['id'];
    points: UsersPoints;
};

export type GameEventNextRound = {
    type: GameEventType.NEXT_ROUND;
    cards: Record<SafeUser['id'], Card[]>;
    round: number;
};

export type GameEventResult = {
    type: GameEventType.RESULT;
    winner: SafeUser['id'];
    points: UsersPoints;
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
    | GameEventRoundResult
    | GameEventNextRound
    | GameEventResult;

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
                playerTurn: user.id,
                winner: undefined,
                round: 1,
                cards: {},
                thrownCards: {},
                trucoPoints: 1,
                points: {},
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

    hasUser(userId: UserId): boolean {
        return this.players.some(player => player.id === userId);
    }

    join(user: SafeUser): Game {
        return this.copy({
            players: [...this.players, user],
        });
    }

    start(): Game {
        const [player1Cards, player2Cards] = generatePlayersCards();
        const points = {
            [this.players[0].id]: 0,
            [this.players[1].id]: 0,
        }
        const cards = {
            [this.players[0].id]: player1Cards,
            [this.players[1].id]: player2Cards,
        }
        const events = [...this.events, this.buildStartEvent(), this.buildNextRoundEvent(this.state.round, cards)];

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

    throwCard(userId: UserId, card: Card, step: GameStep): Game {
        // it should be player turn
        if (this.state.playerTurn !== userId) {
            throw new Error('Not your turn');
        }
        
        // it should be a valid card
        if (!this.state.cards[userId].includes(card)) {
            throw new Error('Invalid card');
        }

        const throwCardEvent = this.buildThrowCardEvent(userId, card, step);

        const updatedGame = this.copy({
            events: [...this.events, throwCardEvent],
            state: {
                ...this.state,
                cards: {
                    ...this.state.cards,
                    [userId]: this.state.cards[userId].filter(c => c !== card),
                },
                thrownCards: {
                    ...this.state.thrownCards,
                    [userId]: [...(this.state.thrownCards[userId] || []), card],
                },
            },
        });

        return updatedGame.withRoundWinnerValidation();
    }

    withRoundWinnerValidation(): Game {
        const winner = getRoundWinner(this.getPlayersIds(), this.state.thrownCards);
        if (winner === undefined) return this;

        const extraPoints = this.state.trucoPoints;
        
        const points = {
            ...this.state.points,
            [winner]: this.state.points[winner] + extraPoints,
        };

        const roundResultEvent = this.buildRoundResultEvent(winner, points);

        const game = this.copy({
            events: [...this.events, roundResultEvent],
            state: {
                ...this.state,
                points,
            },
        });   
        
        return game.withNextRoundOrWin();
    }

    withNextRoundOrWin(): Game {
        if (this.state.points[this.players[0].id] >= 30 || this.state.points[this.players[1].id] >= 30) {
            const winner = this.state.points[this.players[0].id] >= this.state.points[this.players[0].id] ? this.players[0].id : this.players[1].id;

            const gameResultEvent = this.buildResultEvent(winner, this.state.points);

            return this.copy({
                events: [...this.events, gameResultEvent],
                state: {
                    ...this.state,
                    winner,
                },
            });
        }

        const [player1Cards, player2Cards] = generatePlayersCards();
        const cards = {
            [this.players[0].id]: player1Cards,
            [this.players[1].id]: player2Cards,
        }
        const round = this.state.round + 1;

        const nextRoundEvent = this.buildNextRoundEvent(round, cards);

        const firstPlayer = this.state.playerTurn === this.players[0].id ? this.players[1].id : this.players[0].id;

        return this.copy({
            events: [...this.events, nextRoundEvent],
            state: {
                ...this.state,
                round,
                cards,
                firstPlayer,
                thrownCards: {},
                playerTurn: firstPlayer,
                trucoPoints: 1,
            },
        });
    }

    getNewEvents(oldGame: Game): GameEvent[] {
        return this.events.slice(oldGame.events.length);
    }

    buildStartEvent(): GameEventStart {
        return {
            type: GameEventType.START,
        };
    }

    buildNextRoundEvent(round: number, cards: Record<SafeUser['id'], Card[]>): GameEventNextRound {
        return {
            type: GameEventType.NEXT_ROUND,
            round,
            cards,
        };
    }

    buildThrowCardEvent(player: SafeUser['id'], card: Card, step: GameStep): GameEventThrowCard {
        return {
            type: GameEventType.THROW_CARD,
            player,
            card,
            step,
        };
    }

    buildRoundResultEvent(winner: SafeUser['id'], points: UsersPoints): GameEventRoundResult {
        return {
            type: GameEventType.ROUND_RESULT,
            winner,
            points: this.state.points,
        };
    }

    buildResultEvent(winner: SafeUser['id'], points: UsersPoints): GameEventResult {
        return {
            type: GameEventType.RESULT,
            winner,
            points,
        };
    }

    getPlayersIds(): UserId[] {
        return this.players.map(player => player.id);
    }
}
