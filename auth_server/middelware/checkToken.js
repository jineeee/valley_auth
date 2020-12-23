const jwt = require('../modules/jwt');
const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');
const TOKEN_EXPIRED = -3;
const TOKEN_INVALID = -2;

const checkToken = {
    checkToken: async (req, res, next) => {
        var token = req.headers.authorization.split(" ")[1];
        // 토큰 없음
        if (!token) {
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.EMPTY_TOKEN));
        }
        // 토큰 검증
        const user = await jwt.verify(token);
        // 유효기간 만료
        if (user === TOKEN_EXPIRED) {
            return res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.EXPIRED_TOKEN));
        }
        // 유효하지 않는 토큰
        if (user === TOKEN_INVALID) {
            return res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.INVALID_TOKEN));
        }
        // 유효하지 않은 사용자
        if (user.userId === undefined) {
            return res.status(statusCode.UNAUTHORIZED).send(util.fail(statusCode.UNAUTHORIZED, responseMessage.INVALID_TOKEN));
        }
        // payload 값을 req에 담아 전달
        req.userId = user.userId
        next();
    }
}

module.exports = checkToken;