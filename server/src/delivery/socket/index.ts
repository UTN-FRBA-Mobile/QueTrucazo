import { Server, Socket } from 'socket.io';
import { Server as HttpServer } from 'http';

export class SocketManager {
    private io: Server;

    constructor(private server: HttpServer) {
        this.io = new Server(this.server, {
            cors: {
                origin: '*',
                methods: ['GET', 'POST'],
            },
        });

        this.setupSocketEvents();
    }

    public emitTo(room: string, event: string, data: any): void {
        this.io.to(room).emit(event, data);
    }

    private setupSocketEvents(): void {
        this.io.on('connection', (socket: Socket) => {
            socket.on('join-order', (orderId: string) => {
                socket.join(orderId);
            });

            socket.on('increase-item', ({ userId, orderId, itemId }) => {
                console.log(parseInt(userId), parseInt(orderId), parseInt(itemId));
            });

            socket.on('decrease-item', ({ userId, orderId, itemId }) => {
                console.log(parseInt(userId), parseInt(orderId), parseInt(itemId));
            });

            socket.on('update-item-price', ({ userId, orderId, itemId, price }) => {
                console.log(parseInt(userId), parseInt(orderId), parseInt(itemId), parseInt(price));
            });
        });
    }
}