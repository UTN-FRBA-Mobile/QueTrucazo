import { Resource } from "../../../../shared/domain/errors/HandledError";
import { ResourceNotFound } from "../../../../shared/domain/errors/ResourceNotFound";

export class UserNotFound extends ResourceNotFound {
    constructor(id: number) {
        super(Resource.USER, { id });
    }
}