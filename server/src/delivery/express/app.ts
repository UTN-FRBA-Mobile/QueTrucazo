import express, { Application, NextFunction, Request, Response } from 'express';
import { applyMiddlewares } from './middlewares';
import { setupRoutes } from './routes';
import { GetErrorMessage, GetErrorStatusCode } from './utils/Error';
import http from 'http';
import { SocketManager } from '../socket';
import { UserId } from '../../modules/users/core/domain/User';

const { PORT = 8080 } = process.env;

export class ExpressApp {
    private app: Application;
    private server: http.Server;
    private socketManager: SocketManager;

    public static instance: ExpressApp;

    public static getInstance(): ExpressApp {
        if (!ExpressApp.instance) {
            ExpressApp.instance = new ExpressApp();
        }

        return ExpressApp.instance;
    }

    private constructor() {
        this.app = express();
        this.server = http.createServer(this.app);

        this.socketManager = new SocketManager(this.server);

        applyMiddlewares(this.app);
        setupRoutes(this.app);
        this.setupErrorHandler();
    }

    public start(): void {
        this.server.listen(PORT, () => {
            // eslint-disable-next-line no-console
            console.log(`Server is listening on port ${PORT}`);
        });
    }

    public join(room: string, userId: UserId) {
        this.socketManager.join(room, userId);
    }

    public emitToRoom(room: string, event: string, data: any): void {
        this.socketManager.emitToRoom(room, event, data);
    }

    public emitToUser(userId: UserId, event: string, data: any): void {
        this.socketManager.emitToUser(userId, event, data);
    }

    public emitToUsers(userIds: UserId[], event: string, data: any): void {
        this.socketManager.emitToUsers(userIds, event, data);
    }

    private setupErrorHandler(): void {
        this.app.use(
            (error: Error, _: Request, res: Response, next: NextFunction) => {
                res.status(GetErrorStatusCode(error)).json(
                    GetErrorMessage(error)
                );
            }
        );
    }
}
