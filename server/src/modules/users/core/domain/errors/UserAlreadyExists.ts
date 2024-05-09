import { Resource } from "../../../../shared/domain/errors/HandledError";
import { ResourceAlreadyExists } from "../../../../shared/domain/errors/ResourceAlreadyExists";

export class UserAlreadyExists extends ResourceAlreadyExists {
    constructor(username: string) {
        super(Resource.USER, { username });
    }
}