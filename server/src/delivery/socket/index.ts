import { Server, Socket } from 'socket.io';
import { Server as HttpServer } from 'http';
import { createGame } from '../../modules/games/core/actions/CreateGame';
import { UserId } from '../../modules/users/core/domain/User';
import { getUserById } from '../../modules/users/core/actions/GetUserById';
import { joinGame } from '../../modules/games/core/actions/JoinGame';
import { getAllAvailableGames } from '../../modules/games/core/actions/GetAllGames';
import { EnvidoCall, GameId, TrucoCall } from '../../modules/games/core/domain/Game';
import { Card } from '../../modules/games/core/domain/Cards';
import { throwCard } from '../../modules/games/core/actions/ThrowCard';
import { cancelGame } from '../../modules/games/core/actions/CancelGame';
import { goToDeck } from '../../modules/games/core/actions/GoToDeck';
import { envido } from '../../modules/games/core/actions/Envido';
import { answerEnvido } from '../../modules/games/core/actions/AnswerEnvido';
import { playAgain } from '../../modules/games/core/actions/PlayAgain';
import { noPlayAgain } from '../../modules/games/core/actions/NoPlayAgain';
import { truco } from '../../modules/games/core/actions/Truco';
import { answerTruco } from '../../modules/games/core/actions/AnswerTruco';
import { checkUserGame } from '../../modules/games/core/actions/CheckUserGame';
import { checkNoPlayAgain } from '../../modules/games/core/actions/CheckNoPlayAgain';

export class SocketManager {
    private io: Server;
    private sockets: Map<UserId, Socket> = new Map();

    constructor(private server: HttpServer) {
        this.io = new Server(this.server, {
            cors: {
                origin: '*',
                methods: ['GET', 'POST'],
            },
        });

        this.setupSocketEvents();
    }

    public join(room: string, userId: UserId): void {
        this.io.to(room).emit('join', userId);
    }

    public emitToRoom(room: string, event: string, data: any): void {
        this.io.to(room).emit(event, data);
    }

    public emitToUser(userId: UserId, event: string, data: any): void {
        const socket = this.sockets.get(userId);
        if (socket) {
            socket.emit(event, data);
        }
    }

    public emitToUsers(userIds: UserId[], event: string, data: any): void {
        userIds.forEach((userId) => {
            this.emitToUser(userId, event, data);
        });
    }

    private setupSocketEvents(): void {
        this.io.on('connection', (socket: Socket) => {
            console.log('User connected');

            socket.on('register-connection', ({ userId }: { userId: UserId }) => {
                try {
                    console.log('User registered connection', userId);
                    if (this.sockets.has(userId)) {
                        this.sockets.get(userId)?.disconnect();
                    }
                    this.sockets.set(userId, socket);
                    checkUserGame.invoke(userId);
                } catch (error) {
                    console.error('Error registering connection', error);
                }
            });

            socket.on('games-list', async () => {
                try {
                    console.log('fetching games list')
                    const games = await getAllAvailableGames.invoke();
                    socket.emit('games-list', games);
                } catch (error) {
                    console.error('Error fetching games list', error);
                }
            });

            socket.on('create-game', async ({ userId }: { userId: UserId }) => {
                try {
                    console.log('creating game')
                    const user = await getUserById.invoke(userId);
                    await createGame.invoke(user);
                } catch (error) {
                    console.error('Error creating game', error);
                }
            });

            socket.on('join-game', async ({ userId, gameId }: { userId: UserId, gameId: GameId }) => {
                try {
                    console.log('joining game')
                    const user = await getUserById.invoke(userId);
                    await joinGame.invoke(gameId, user);
                } catch (error) {
                    console.error('Error joining game', error);
                }
            });

            socket.on('cancel-game', async ({ userId, gameId }: { userId: UserId, gameId: GameId }) => {
                try {
                    console.log('cancelling game')
                    await cancelGame.invoke(gameId, userId);
                } catch (error) {
                    console.error('Error cancelling game', error);
                }
            });

            socket.on('throw-card', async ({ userId, gameId, card }: { userId: UserId, gameId: GameId, card: Card }) => {
                try {
                    console.log('throwing card')
                    await throwCard.invoke(gameId, userId, card);
                } catch (error) {
                    console.error('Error throwing card', error);
                }
            });

            socket.on('go-to-deck', async ({ userId, gameId }: { userId: UserId, gameId: GameId }) => {
                try {
                    console.log('going to deck')
                    await goToDeck.invoke(gameId, userId);
                } catch (error) {
                    console.error('Error going to deck', error);
                }
            });

            socket.on('envido', async ({ userId, gameId, call }: { userId: UserId, gameId: GameId, call: EnvidoCall }) => {
                try {
                    console.log('envido')
                    if (call !== EnvidoCall.ENVIDO && call !== EnvidoCall.REAL_ENVIDO && call !== EnvidoCall.FALTA_ENVIDO) {
                        throw new Error('Invalid envido call');
                    }
                    await envido.invoke(gameId, userId, call);
                } catch (error) {
                    console.error('Error envido', error);
                }
            });

            socket.on('answer-envido', async ({ userId, gameId, accepted }: { userId: UserId, gameId: GameId, accepted: boolean }) => {
                try {
                    console.log('responder envido')
                    await answerEnvido.invoke(gameId, userId, accepted);
                } catch (error) {
                    console.error('Error envido', error);
                }
            });

            socket.on('truco', async ({ userId, gameId, call }: { userId: UserId, gameId: GameId, call: TrucoCall }) => {
                try {
                    console.log('truco')
                    await truco.invoke(gameId, userId, call);
                } catch (error) {
                    console.error('Error truco', error);
                }
            });

            socket.on('answer-truco', async ({ userId, gameId, accepted }: { userId: UserId, gameId: GameId, accepted: boolean }) => {
                try {
                    console.log('responder truco')
                    await answerTruco.invoke(gameId, userId, accepted);
                } catch (error) {
                    console.error('Error answer truco', error);
                }
            });

            socket.on('play-again', async ({ userId, gameId }: { userId: UserId, gameId: GameId }) => {
                try {
                    console.log('playing again')
                    await playAgain.invoke(gameId, userId);
                } catch (error) {
                    console.error('Error playing again', error);
                }
            });

            socket.on('no-play-again', async ({ userId, gameId }: { userId: UserId, gameId: GameId }) => {
                try {
                    console.log('no playing again')
                    await noPlayAgain.invoke(gameId, userId);
                } catch (error) {
                    console.error('Error no playing again', error);
                }
            });

            socket.on('disconnect', () => {
                try {
                    const userId = Array.from(this.sockets.entries()).find(([_, s]) => s === socket)?.[0];
                    if (userId) {
                        this.sockets.delete(userId);
                        console.log('User disconnected', userId);
                        checkNoPlayAgain.invoke(userId);
                    }
                } catch (error) {
                    console.error('Error disconnecting', error);
                    
                }
            });
        });
    }
}