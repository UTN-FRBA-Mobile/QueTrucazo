import { NextFunction, Request, Response } from "express";

type Middleware = (req: Request, res: Response, next: NextFunction) => (Promise<void> | void);

export const defaultMiddleware: (middleware: Middleware) => Middleware = (middleware: Middleware) => {
    return async (req, res, next) => {
        try {
            await middleware(req, res, next);
        } catch (error: any) {
            next(error);
        }
    }
}

export default Middleware;