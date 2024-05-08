import { ErrorType, HandledError, Resource } from './HandledError';

export class ResourceAlreadyExists extends HandledError {
  constructor(resource: Resource, params: any) {
    super({
      type: ErrorType.RESOURCE_ALREADY_EXISTS,
      params: {
        resource,
        ...params,
      },
    });
  }
}
