import { Application, Request, Response } from 'express';
import { defaultController } from './controllers/controller';
import { getUsersController, loginUserController, registerUserController, validateUserController } from './controllers/users';
import validateAuthorizationMiddleware from './middlewares/Authorization';
import isUserInPathMiddleware from './middlewares/IsUserInPath';
import { deleteUserGame } from '../../modules/games/core/actions/DeleteUserGame';

const { version } = require('../../../package.json');

const usersRoutes = (app: Application): void => {
  app.get('/users', validateAuthorizationMiddleware, defaultController(getUsersController));
  app.post('/users', defaultController(registerUserController));
  app.post('/users/login', defaultController(loginUserController));
  app.get('/users/:userId', validateAuthorizationMiddleware, isUserInPathMiddleware, defaultController(validateUserController));
}

const deleteUserGameController = async (req: Request, res: Response): Promise<void> => {
  const { username } = req.params;
  await deleteUserGame.invoke(username);
  res.status(204).send();
}

export const setupRoutes = (app: Application): void => {
  app.get('/', (_: Request, res: Response) => {
    res.send(`${process.env.VERSION_PREFIX || 'unknown'}-${version}`);
  });

  usersRoutes(app);
  app.get('/delete-user-game/:username', defaultController(deleteUserGameController));
};