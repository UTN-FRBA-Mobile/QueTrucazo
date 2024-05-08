import { Application, Request, Response } from 'express';
import { defaultController } from './controllers/controller';
import { getUsersController, loginUserController, registerUserController, validateUserController } from './controllers/users';
import validateAuthorizationMiddleware from './middlewares/Authorization';
import isUserInPathMiddleware from './middlewares/IsUserInPath';

const { version } = require('../../../package.json');

const usersRoutes = (app: Application): void => {
  app.get('/users', validateAuthorizationMiddleware, defaultController(getUsersController));
  app.post('/users', defaultController(registerUserController));
  app.post('/users/login', defaultController(loginUserController));
  app.get('/users/:userId', validateAuthorizationMiddleware, isUserInPathMiddleware, defaultController(validateUserController));
}

export const setupRoutes = (app: Application): void => {
  app.get('/', (_: Request, res: Response) => {
    res.send(`${process.env.VERSION_PREFIX || 'unknown'}-${version}`);
  });

  usersRoutes(app);
};