import { Request, Response, NextFunction } from "express";
import Middleware, { defaultMiddleware } from "./middleware";
import { getUserById } from "../../../modules/users/core/actions/GetUserById";
import { tokenService } from "../../../modules/users/core/domain/TokenService";

/**
 * Express middleware, checks for a valid JSON Web Token and returns 401 Unauthorized if one isn't found.
 */
const validateAuthorizationMiddleware: Middleware = async (request: Request, response: Response, next: NextFunction) => {
    const unauthorized = (message: string) => response.status(401).json({
        type: "UNAUTHORIZED",
        params: {
            message: message
        }
    });

    const requestHeader = "Authorization";
    const token = request.get(requestHeader);
    
    if (!token) {
        unauthorized(`Header ${requestHeader} not found`);
        return;
    }

    const validateResult = tokenService.validate(token.replace("Bearer ", "")); 

    if (!validateResult.valid) {
        unauthorized(validateResult.message || 'Invalid token');
        return;
    }

    if (!validateResult.tokenDecoded?.session?.id) {
        unauthorized('Invalid token - User ID not found');
        return;
    }

    const user = await getUserById.invoke(validateResult.tokenDecoded?.session?.id);

    response.locals = {
        ...response.locals,
        session: validateResult.tokenDecoded?.session,
        user,
    };

    next();
}

export default defaultMiddleware(validateAuthorizationMiddleware);