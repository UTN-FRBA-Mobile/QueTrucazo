export enum ErrorType {
  RESOURCE_ALREADY_EXISTS = 'RESOURCE_ALREADY_EXISTS',
  RESOURCE_NOT_FOUND = 'RESOURCE_NOT_FOUND',
  INVALID_ENUM_VALUE = 'INVALID_ENUM_VALUE',
  INVALID_STATE = 'INVALID_STATE',
  FORM_INVALID_FIELD = 'FORM_INVALID_FIELD',
  USER_AND_TOKEN_MISMATCH = 'USER_AND_TOKEN_MISMATCH',
  UNKNOWN = 'UNKNOWN',
  ACCESS_DENIED = 'ACCESS_DENIED',
  BAD_REQUEST = 'BAD_REQUEST',
  INVALID_VERSION = 'INVALID_VERSION',
}

export type HandledErrorParams = {
  type: ErrorType;
  params: Record<string, any>;
};

export enum Resource {
  GAME = 'game',
  USER = 'user',
}

export class HandledError extends Error {
  private error: HandledErrorParams;

  constructor(error: HandledErrorParams) {
    super(`[ERROR] ${error.type}: ${JSON.stringify(error.params)}`);
    this.error = error;
    this.name = 'HandledError';
  }

  public getMessage(): HandledErrorParams {
    return this.error;
  }
}
