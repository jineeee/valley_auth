var express = require('express');
var router = express.Router();
const adminController = require('../Controller/AdminController');
const checkToken = require('../middelware/checkToken').checkToken;

router.get('/', checkToken, adminController.readUsers);
router.put('/', checkToken, adminController.updateAdmin);
router.post('/update', checkToken, adminController.updateUserInfo);
router.get('/:id', checkToken, adminController.readUser);

module.exports = router;
