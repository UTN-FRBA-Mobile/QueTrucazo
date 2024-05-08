import { FormInvalidField } from "../../../../shared/domain/errors/FormInvalidField";
import { Resource } from "../../../../shared/domain/errors/HandledError";

export class LoginError extends FormInvalidField {
    constructor() {
        super(Resource.USER, { email: "LOGIN_INVALID" });
    }
}