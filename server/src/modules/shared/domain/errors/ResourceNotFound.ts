import { ErrorType, HandledError, Resource } from './HandledError';

export class ResourceNotFound extends HandledError {
  constructor(resource: Resource, id: any) {
    super({
      type: ErrorType.RESOURCE_NOT_FOUND,
      params: {
        resource,
        id,
      },
    });
  }
}
