const nodemailer = require('nodemailer');
const smtpPool = require('nodemailer-smtp-pool');
const emailConfig = require('../config/emailConfig');

const responseMessage = require('../modules/responseMessage');
const statusCode = require('../modules/statusCode');
const util = require('../modules/util');

// 랜덤 생성된 인증번호 값
const rand = Math.floor(Math.random() * 1000000)+100000;

module.exports = {
    // 인증 이메일 전송
    emailVerify : async(req, res) => {
        // 이메일 확인
        if(!req.body.email || !req.body.email.includes("smilegate.com")){
            res.status(statusCode.BAD_REQUEST).send(util.fail(statusCode.BAD_REQUEST, responseMessage.NOT_VALID_EMAIL));
            return;
        }

        const from = 'AUTH_SERVER';
        const to = req.body.email;
        const subject = '관리자 인증 메일입니다';
        const rand = Math.floor(Math.random() * 1000000)+100000;
        const html = '<p>인증번호는 '+ rand + ' 입니다.\n 인증번호 창에 입력해주세요.';
    
        const mailOptions = {
            from,
            to,
            subject,
            html
        };
        
        const config = emailConfig.emailConfig;
        const transporter = nodemailer.createTransport(smtpPool({
            service: config.mailer.service,
            host: config.mailer.host,
            port: config.mailer.port,
            auth: {
                user: config.mailer.user,
                pass: config.mailer.password,
            },
            tls: {
                rejectUnauthorize: false,
            },
            maxConnections: 5,
            maxMessages: 10,
        }));

        try{
            transporter.sendMail(mailOptions);
            // 성공
            res.status(statusCode.OK).send(util.success(statusCode.OK, responseMessage.SEND_EMAIL, rand));
            return;
        }catch(err){
            res.status(statusCode.INTERNAL_SERVER_ERROR).send(util.fail(statusCode.INTERNAL_SERVER_ERROR, responseMessage.SEND_EMAIL_FAIL));
            return;
        }
    }
}