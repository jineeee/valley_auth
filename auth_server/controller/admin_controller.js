const responseMessage = require('../modules/response_message');
const statusCode = require('../modules/status_code');
const util = require('../modules/util');
const adminModel = require('../model/admin');
const redisClient = require('../modules/redis');

module.exports = {
    // 관리자 정보 업데이트
    updateAdmin: async (req, res) => {
        const id = req.userId
        try {
            const result = await adminModel.updateAdmin(id);
            // 성공
            if (result == 1) {
                res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.UPDATE_SUCCESS));
                return;
            }
            // DB 저장 실패
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.UPDATE_FAIL));
            return;
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // 유저 전체 조회
    readUsers: async (req, res) => {
        try {
            const result = await adminModel.readUsers();
            res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.READ_SUCCESS, result));
            return;
        } catch (err) {
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, err.message));
            return;
        }
    },
    // 개별 유저 정보 조회
    readUser: async (req, res) => {
        const userId = req.params.id;
        redisClient.hget('userInfo', userId, function (err, data) {
            if (err) {
                res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, err.message));
                return;
            }
            data = JSON.parse(data);
            if (data != null) {
                res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.READ_SUCCESS, data[0]));
                return;
            }
        });
        try {
            const result = await adminModel.readUser(userId);
            redisClient.hset('userInfo', userId, JSON.stringify(result));
            res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.READ_SUCCESS, result[0]));
            return;
        } catch (err) {
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
            return;
        }
    },
    // 유저 정보 수정
    updateUserInfo: async (req, res) => {
        const {
            id,
            name,
            department,
            rank
        } = req.body;
        // NULL 값 확인
        if (!id || !name || !department || !rank) {
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
        }

        try {
            const data = {
                id,
                name,
                department,
                rank
            };
            const result = await adminModel.updateUserInfo(data);
            if (result != 0) {
                return res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.UPDATE_SUCCESS));
            }
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.UPDATE_FAIL));
        } catch (err) {
            return res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, err.message));
        }
    }
}
