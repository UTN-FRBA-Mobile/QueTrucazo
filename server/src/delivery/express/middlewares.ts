import express, { Application, Request, Response, NextFunction } from 'express';
import bodyParser from 'body-parser';
import cors from 'cors';

const logMiddleware = (req: Request, res: Response, next: NextFunction) => {
  const current_datetime = new Date();
  const formatted_date =
    current_datetime.getFullYear() +
    '-' +
    (current_datetime.getMonth() + 1) +
    '-' +
    current_datetime.getDate() +
    ' ' +
    current_datetime.getHours() +
    ':' +
    current_datetime.getMinutes() +
    ':' +
    current_datetime.getSeconds();
  const method = req.method;
  const url = req.url;
  const log = `[${formatted_date}] ${method} ${url}`;
  // eslint-disable-next-line no-console
  console.log(log);
  next();
};

export const applyMiddlewares = (app: Application): void => {
  app.use(cors());
  app.use(express.json());
  app.use(bodyParser.json());
  if (process.env.NODE_ENV === 'development' || process.env.LOG_REQUESTS === 'true') {
    app.use(logMiddleware);
  }
};
