const responseMessage = require('../modules/response_message');
const statusCode = require('../modules/status_code');
const util = require('../modules/util');
const userModel = require('../model/user');
const crypto = require('../modules/crypto');
const jwt = require('../modules/jwt');
const redis = require('redis');
const redisClient = require('../modules/redis');

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
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
            return;
        }
        // ID 중복 확인
        if (await userModel.idCheck(req.body.id)) {
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.DUPLICATE_ID));
            return;
        }

        try {
            const {
                salt,
                hashed
            } = await crypto.encrypt(pw);
            const data = {
                id,
                salt,
                hashed,
                name,
                department,
                rank
            };
            const idx = await userModel.signUp(data);
            // DB 저장 실패
            if (idx == -1) {
                res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.DB_ERROR));
                return;
            }
            // 성공
            return res.status(statusCode.CREATED).send(util.successWithoutData(statusCode.OK, responseMessage.CREATE_USER_SUCCESS));
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // 로그인
    signIn: async (req, res) => {
        const {
            id,
            pw
        } = req.body;
        // 값 확인
        if (!id || !pw) {
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
            return;
        }
        try {
            const userResult = await userModel.signIn(id);
            // 존재하지 않는 계정
            if (userResult[0] === undefined) {
                res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NO_USER));
                return;
            }
            const hashed = await crypto.encryptWithSalt(pw, userResult[0].salt);
            // 비밀번호 불일치
            if (hashed !== userResult[0].hashed) {
                res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.MISS_MATCH_PW));
                return;
            }
            const {
                token,
                refreshToken
            } = await jwt.sign(userResult[0]);

            // 성공
            res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.LOGIN_SUCCESS, {
                // id: id,
                admin: userResult[0].admin,
                accessToken: token,
                refreshToken: refreshToken
            }));
            return;
        } catch (err) {
            console.error(err);
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // 새 access token 발급
    newToken: async (req, res) => {
        try {
            const refreshToken = req.body.refreshToken;
            // 토큰 없음
            if (!refreshToken) {
                res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.EMPTY_TOKEN));
                return;
            }
            const user = await jwt.verify(refreshToken);
            // 만료된 토큰
            if (user == -3) {
                res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.EXPIRED_TOKEN));
                return;
                // 유효하지 않은 토큰
            } else if (user == -2) {
                res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.INVALID_TOKEN));
                return;
            }
            // 새 access token 발급
            const newToken = jwt.refresh(user);
            res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.LOGIN_SUCCESS, newToken));
            return;
        } catch (err) {
            console.log(err);
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // ID 확인
    verifyUser: async (req, res) => {
        try {
            const result = await userModel.idCheck(req.params.id)
            if (result) {
                res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.READ_SUCCESS));
                return;
            }
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NO_USER));
            return;
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // 비밀번호 변경
    changePassword: async (req, res) => {
        const id = req.userId
        const pw = req.body.pw;
        const newPw = req.body.newPw;

        try {
            const userResult = await userModel.signIn(id);
            // 존재하지 않는 계정
            if (userResult[0] === undefined) {
                res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NO_USER));
                return;
            }

            var hashed = await crypto.encryptWithSalt(pw, userResult[0].salt);
            // 비밀번호 불일치
            if (hashed !== userResult[0].hashed) {
                res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.MISS_MATCH_PW));
                return;
            }

            var {
                salt,
                hashed
            } = await crypto.encrypt(newPw);
            const data = {
                id,
                salt,
                hashed
            };
            const result = await userModel.changePassword(data);
            if (result != 0) {
                res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.UPDATE_SUCCESS));
                return;
            }
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.DB_ERROR));
            return;
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    }
}
