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

export const getPlayersCards = (): [Card[], Card[]] => {
    // get cards one by one and complete two arrays of three cards
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