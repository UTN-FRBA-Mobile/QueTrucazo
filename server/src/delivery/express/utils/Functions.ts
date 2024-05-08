import { Request, Response } from "express"
import { BadRequest } from "../../../modules/shared/domain/errors/BadRequest";
import { User } from "../../../modules/users/core/domain/User";

export enum PathParams {
    ORDER_ID = 'orderId',
    USER_ID = 'userId',
}

const getOptionalPathParam = (req: Request, param: PathParams): string | undefined => {
    return req.params[param];
}

const getPathParam = (req: Request, param: PathParams): string => {
    const value = getOptionalPathParam(req, param);
    if (!value) {
        throw new BadRequest(`${param} not found`);
    }
    return value;
}

export const getUser = (res: Response): User => {
    const user = res.locals.user as User | undefined;
    if (!user) {
        throw new BadRequest('User not found');
    }
    return user;
}

export const getUserId = (res: Response): User['id'] => {
    return getUser(res).id;
}

export const getIntPathParam = (req: Request, param: PathParams): number => {
    const value = getPathParam(req, param);
    const parsed = parseInt(value);
    if (isNaN(parsed)) {
        throw new BadRequest(`${param} is not a number`);
    }
    return parsed;
}

const getOptionalIntPathParam = (req: Request, param: PathParams): number | undefined => {
    const value = getOptionalPathParam(req, param);
    if (!value) return undefined;
    const parsed = parseInt(value);
    if (isNaN(parsed)) {
        throw new BadRequest(`${param} is not a number`);
    }
    return parsed;
}

export const getPage = (req: Request): number => {
    if (!req.query.page) {
        return 1;
    }
    const page = parseInt(req.query.page as string);
    if (isNaN(page)) {
        throw new BadRequest('Invalid page');
    }
    return page;
}

export const getPageSize = (req: Request): number => {
    if (!req.query.pageSize) {
        return 10;
    }
    const pageSize = parseInt(req.query.pageSize as string);
    if (isNaN(pageSize)) {
        throw new BadRequest('Invalid page size');
    }
    if (pageSize > 100) {
        throw new BadRequest('Page size cannot be greater than 100');
    }
    return pageSize;
}
