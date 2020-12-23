var express = require('express');
var router = express.Router();
const adminController = require('../controller/admin_controller');
const checkToken = require('../middelware/check_token').checkToken;

router.get('/', checkToken, adminController.readUsers);
router.put('/', checkToken, adminController.updateAdmin);
router.put('/update', checkToken, adminController.updateUserInfo);
router.get('/:id', checkToken, adminController.readUser);

module.exports = router;
