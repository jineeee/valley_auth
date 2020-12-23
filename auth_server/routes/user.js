var express = require('express');
var router = express.Router();
const userController = require('../Controller/user_controller');
const emailController = require('../Controller/email_controller');
const checkToken = require('../middelware/check_token').checkToken;

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.post('/signup', userController.signUp);
router.post('/signin', userController.signIn);
router.post('/newToken', userController.newToken);
router.get('/:id', userController.verifyUser)
router.post('/changePassword', checkToken, userController.changePassword);
router.post('/findPassword', emailController.tempPassword);
router.post('/verifyEmail', checkToken, emailController.verifyEmail);


module.exports = router;
