const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');
const adminModel = require('../model/admin');
const jwt = require('../modules/jwt');

module.exports = {
    // 관리자 정보 업데이트
    updateAdmin : async(req, res) => {
        const user = await jwt.verify(req.headers.token);
        const id = user.user_id;
        try{
            const result = await adminModel.updateAdmin(id);
            if(result==1){
                return res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.UPDATE_SUCCESS));
            }
            return res.status(statusCode.DB_ERROR).send(util.fail(statusCode.DB_ERROR, responseMessage.UPDATE_FAIL));
        }catch(err){
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, err.message));
        }
    },
    // 유저 전체 조회
    readUsers : async(req, res) => {
        try{
            const result = await adminModel.readUsers();
            console.log(result)
            return res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.READ_SUCCESS, result));
        }catch(err){
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, err.message));
        }
    },
    // 유저 정보 수정
    updateUserInfo : async(req, res) => {
        console.log(req.body)
        const {id, name, department, rank } = req.body;
        // NULL 값 확인
        if (!id || !name || !department || !rank) {
            return res.status(statusCode.NO_CONTENT).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NULL_VALUE));
        }

        try{
            const data = {id, name, department, rank};
            const result = await adminModel.updateUserInfo(data);
            if(result!=0){
                return res.status(statusCode.OK).send(util.successWithoutData(statusCode.OK, responseMessage.UPDATE_SUCCESS));
            }
            return res.status(statusCode.DB_ERROR).send(util.fail(statusCode.DB_ERROR, responseMessage.UPDATE_FAIL));
        }catch(err){
            return res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, err.message));
        }
    }
}
