import { Resource } from "../../../../shared/domain/errors/HandledError";
import { ResourceNotFound } from "../../../../shared/domain/errors/ResourceNotFound";

export class GameNotFound extends ResourceNotFound {
    constructor(id: number) {
        super(Resource.GAME, { id });
    }
}