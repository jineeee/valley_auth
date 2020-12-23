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
            console.err(err);
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
