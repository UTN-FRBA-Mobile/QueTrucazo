import { Server, Socket } from 'socket.io';
import { Server as HttpServer } from 'http';
import { createGame } from '../../modules/games/core/actions/CreateGame';
import { UserId } from '../../modules/users/core/domain/User';
import { getUserById } from '../../modules/users/core/actions/GetUserById';
import { joinGame } from '../../modules/games/core/actions/JoinGame';
import { getAllAvailableGames } from '../../modules/games/core/actions/GetAllGames';
import { GameId, GameStep } from '../../modules/games/core/domain/Game';
import { Card } from '../../modules/games/core/domain/Cards';
import { throwCard } from '../../modules/games/core/actions/ThrowCard';
import { cancelGame } from '../../modules/games/core/actions/CancelGame';

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
                    this.sockets.set(userId, socket);
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

            socket.on('throw-card', async ({ userId, gameId, card, step }: { userId: UserId, gameId: GameId, card: Card, step: GameStep }) => {
                try {
                    console.log('throwing card')
                    await throwCard.invoke(gameId, userId, card, step);
                } catch (error) {
                    console.error('Error throwing card', error);
                }
            });
        });
    }
}