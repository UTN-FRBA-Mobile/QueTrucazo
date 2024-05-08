import { ErrorType, HandledError, Resource } from './HandledError';

export class BadRequest extends HandledError {
  constructor(message: string) {
    super({
      type: ErrorType.BAD_REQUEST,
      params: {
        message,
      },
    });
  }
}
