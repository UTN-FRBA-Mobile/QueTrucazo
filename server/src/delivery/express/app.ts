import express, { Application, NextFunction, Request, Response } from 'express';
import { applyMiddlewares } from './middlewares';
import { setupRoutes } from './routes';
import { GetErrorMessage, GetErrorStatusCode } from './utils/Error';
import http from 'http';
import { SocketManager } from '../socket';

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

    public emit(room: string, event: string, data: any): void {
        this.socketManager.emitTo(room, event, data);
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
