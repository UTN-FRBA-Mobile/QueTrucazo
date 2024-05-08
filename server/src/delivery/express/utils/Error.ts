import {
  ErrorType,
  HandledError,
} from '../../../modules/shared/domain/errors/HandledError';

export const GetErrorStatusCode = (error: Error): number => {
  if (error instanceof HandledError) {
    switch (error.getMessage().type) {
      case ErrorType.BAD_REQUEST:
        return 400;
      case ErrorType.UNKNOWN:
        return 500;
      case ErrorType.INVALID_VERSION:
        return 409;
      case ErrorType.RESOURCE_ALREADY_EXISTS:
        return 409;
      case ErrorType.RESOURCE_NOT_FOUND:
        return 404;
      case ErrorType.INVALID_ENUM_VALUE:
        return 400;
      case ErrorType.USER_AND_TOKEN_MISMATCH:
        return 403;
      case ErrorType.INVALID_STATE:
        return 400;
      case ErrorType.FORM_INVALID_FIELD:
        return 400;
      case ErrorType.ACCESS_DENIED:
        return 403;
      default:
        return 500;
    }
  }

  return 500;
};

const getError = (error: Error): any => {
  if (error instanceof HandledError) {
    return error.getMessage();
  }

  return {
    type: ErrorType.UNKNOWN,
    params: {
      message: error.message,
    }
  };
};

export const GetErrorMessage = (e: Error): any => {
  const error = getError(e);
  console.log(error);
  return error;
};
