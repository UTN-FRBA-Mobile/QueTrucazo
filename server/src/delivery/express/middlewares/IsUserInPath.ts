import { Request, Response, NextFunction } from "express";
import { PathParams, getIntPathParam, getUserId } from "../utils/Functions";
import { HandledError, ErrorType } from "../../../modules/shared/domain/errors/HandledError";
import Middleware, { defaultMiddleware } from "./middleware";

const isUserInPathMiddleware: Middleware = (req: Request, res: Response, next: NextFunction) => {
    if (getUserId(res) !== getIntPathParam(req, PathParams.USER_ID))
        throw new HandledError({
            type: ErrorType.USER_AND_TOKEN_MISMATCH,
            params: {}
        })

    next();
}

export default defaultMiddleware(isUserInPathMiddleware);
