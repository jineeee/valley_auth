const randToken = require('rand-token');
const jwt = require('jsonwebtoken');
const secretKey = require('../config/secret_key').secretKey;
const option = require('../config/secret_key').option;
const refreshOption = require('../config/secret_key').refreshOption;
const TOKEN_EXPIRED = -3;
const TOKEN_INVALID = -2;

module.exports = {
    // access token 발급
    sign: async (user) => {
        const payload = {
            userId: user.id
        };
        const result = {
            token: jwt.sign(payload, secretKey, option),
            refreshToken: jwt.sign(payload, secretKey, refreshOption)
        };
        return result;
    },
    verify: async (token) => {
        let decoded;
        try {
            decoded = jwt.verify(token, secretKey);
        } catch (err) {
            if (err.message === 'jwt expired') {
                console.log('expired token');
                return TOKEN_EXPIRED;
            } else if (err.message === 'invalid token') {
                console.log('invalid token');
                console.log(TOKEN_INVALID);
                return TOKEN_INVALID;
            } else {
                console.log('invalid token');
                return TOKEN_INVALID;
            }
        }
        return decoded;
    },
    refresh: (user) => {
        const payload = {
            userId: user.userId
        };
        return jwt.sign(payload, secretKey, option);
    }
}