import { BadRequest } from "../../../modules/shared/domain/errors/BadRequest";
import { createUser } from "../../../modules/users/core/actions/CreateUser";
import { getAllUsers } from "../../../modules/users/core/actions/GetAllUsers";
import { getUserById } from "../../../modules/users/core/actions/GetUserById";
import { getUserToken } from "../../../modules/users/core/actions/GetUserToken";
import { loginUser } from "../../../modules/users/core/actions/LoginUser";
import { SafeUser } from "../../../modules/users/core/domain/User";
import { getIntPathParam, PathParams } from "../utils/Functions";
import Controller from "./controller";

export const getUsersController: Controller = async (req, res) => {
    const result = await getAllUsers.invoke();

    res.status(200).json(result);
}

export const registerUserController: Controller = async (req, res) => {
    const { email, password } = req.body;

    const user: SafeUser = await createUser.invoke({ email, password });
    const token = getUserToken.invoke(user);

    res.status(201).json({
        user,
        token
    });
};

export const loginUserController: Controller = async (req, res) => {
    const { email, password } = req.body;

    const user: SafeUser = await loginUser.invoke(email, password);
    const token = getUserToken.invoke(user);

    res.status(201).json({
        user,
        token,
    });
}

export const validateUserController: Controller = async (req, res) => {
    const id = getIntPathParam(req, PathParams.USER_ID);
    const user = await getUserById.invoke(Number(id));
    const token = getUserToken.invoke(user);
    res.status(200).json({ user, token });
}