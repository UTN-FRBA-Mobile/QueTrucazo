import { UserId } from "../../../users/core/domain/User";

export type Card =
    'O1' | 'O2' | 'O3' | 'O4' | 'O5' | 'O6' | 'O7' | 'O10' | 'O11' | 'O12'
    | 'E1' | 'E2' | 'E3' | 'E4' | 'E5' | 'E6' | 'E7' | 'E10' | 'E11' | 'E12'
    | 'B1' | 'B2' | 'B3' | 'B4' | 'B5' | 'B6' | 'B7' | 'B10' | 'B11' | 'B12'
    | 'C1' | 'C2' | 'C3' | 'C4' | 'C5' | 'C6' | 'C7' | 'C10' | 'C11' | 'C12';

export const deck: Card[] = [
    'O1', 'O2', 'O3', 'O4', 'O5', 'O6', 'O7', 'O10', 'O11', 'O12',
    'E1', 'E2', 'E3', 'E4', 'E5', 'E6', 'E7', 'E10', 'E11', 'E12',
    'B1', 'B2', 'B3', 'B4', 'B5', 'B6', 'B7', 'B10', 'B11', 'B12',
    'C1', 'C2', 'C3', 'C4', 'C5', 'C6', 'C7', 'C10', 'C11', 'C12'
]

export const generatePlayersCards = (): [Card[], Card[]] => {
    const currentDeck = [...deck];
    const player1: Card[] = [];
    const player2: Card[] = [];
    for (let i = 0; i < 6; i++) {
        const randomIndex = Math.floor(Math.random() * currentDeck.length);
        if (i % 2 === 0) {
            player1.push(currentDeck[randomIndex]);
        } else {
            player2.push(currentDeck[randomIndex]);
        }
        currentDeck.splice(randomIndex, 1);
    }
    return [player1, player2];
}

export const getCardValue = (card: Card): number => {
    switch (card) {
        case 'O4':
        case 'E4':
        case 'B4':
        case 'C4':
            return 0;
        case 'O5':
        case 'E5':
        case 'B5':
        case 'C5':
            return 1;
        case 'O6':
        case 'E6':
        case 'B6':
        case 'C6':
            return 2;
        case 'B7':
        case 'C7':
            return 3;
        case 'O10':
        case 'E10':
        case 'B10':
        case 'C10':
            return 4;
        case 'O11':
        case 'E11':
        case 'B11':
        case 'C11':
            return 5;
        case 'O12':
        case 'E12':
        case 'B12':
        case 'C12':
            return 6;
        case 'O1':
        case 'C1':
            return 7;
        case 'O2':
        case 'C2':
        case 'E2':
        case 'B2':
            return 8;
        case 'O3':
        case 'C3':
        case 'E3':
        case 'B3':
            return 9;
        case 'O7':
            return 10;
        case 'E7':
            return 11;
        case 'B1':
            return 12;
        case 'E1':
            return 13;
        default:
            return 0;
    }
}

export type WinnerResult = -1 | 0 | 1;

export const getStepWinner = (players: UserId[], cards: Record<UserId, Card[]>, step: 1 | 2 | 3): WinnerResult | undefined => {
    if (players.some((player) => !cards[player] || cards[player].length < step)) {
        return undefined;
    }
    const player1CardValue = getCardValue(cards[players[0]][step - 1]);
    const player2CardValue = getCardValue(cards[players[1]][step - 1]);

    if (player1CardValue === player2CardValue) {
        return -1;
    }

    return player1CardValue > player2CardValue ? 0 : 1;
}

export const getRoundWinner = (players: UserId[], cards: Record<UserId, Card[]>): UserId | undefined => {
    const step1Winner = getStepWinner(players, cards, 1);
    if (step1Winner === undefined) {
        return undefined;
    }

    const step2Winner = getStepWinner(players, cards, 2);
    if (step2Winner === undefined) {
        return undefined;
    }

    if (step1Winner === step2Winner) {
        return players[step1Winner];
    }

    if (step1Winner === -1 && step2Winner !== -1) {
        return players[step2Winner];
    }

    if (step2Winner === -1 && step1Winner !== -1) {
        return players[step1Winner];
    }

    const step3Winner = getStepWinner(players, cards, 3);
    if (step3Winner === undefined) {
        return undefined;
    }
    if (step3Winner === -1) {
        return players[step1Winner];
    }
    return players[step3Winner];
}