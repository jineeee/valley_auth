const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');
const userModel = require('../model/user');
const crypto = require('../modules/crypto');
const jwt = require('../modules/jwt');

module.exports = {
    // 회원 가입
    signUp: async (req, res) => {
        const {
            id,
            pw,
            name,
            department,
            rank
        } = req.body;
        // NULL 값 확인
        if (!id || !pw || !name || !department || !rank) {
            return res.status(statusCode.NO_CONTENT).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
        }
        // ID 중복 확인
        if (await userModel.idCheck(req.body.id)) {
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.DUPLICATE_ID));
        }

        try {
            const {salt, hashed} = await crypto.encrypt(pw);
            const data = {
                id,
                salt,
                hashed,
                name,
                department,
                rank
            };
            const idx = userModel.signUp(data);
            if (idx == -1) {
                return res.status(statusCode.DB_ERROR).send(util.fail(statusCode.DB_ERROR, responseMessage.DB_ERROR));
            }
            return res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.CREATE_USER_SUCCESS));
        } catch (err) {
            return res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            throw err;
        }

    },
    // 로그인
    signIn: async (req, res) => {
        const {id, pw} = req.body;
        console.log('sign in req : ', req.body)
        // 값 확인
        if (!id || !pw) {
            return res.status(statusCode.OK).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
        }

        try {
            const userResult = await userModel.signIn(req);
            if (userResult[0] === undefined) {
                return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NO_USER));
            }

            const hashed = await crypto.encryptWithSalt(pw, userResult[0].salt);
            if (hashed !== userResult[0].hashed) {
                return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.MISS_MATCH_PW));
            }
            const {token, refreshToken} = await jwt.sign(userResult[0]);

            console.log(userResult)
            // 성공
            return res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.LOGIN_SUCCESS, {
                id: id,
                admin: userResult[0].admin,
                accessToken: token, 
                refreshToken: refreshToken
            }));

        } catch (err) {
            return res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            throw err;
        }
    }
}
