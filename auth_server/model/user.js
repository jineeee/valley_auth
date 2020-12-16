const pool = require('../modules/pool');

module.exports = {
    signUp : async(data) => {
        const query = `INSERT INTO user(id, hashed, salt, name, department, \`rank\`) VALUES ('${data.id}', '${data.hashed}', '${data.salt}', '${data.name}', '${data.department}', '${data.rank}')`;
        try{
            const result = await pool.queryParam(query);
            return result;
        }catch(err){
            console.log('SIGN UP ERROR : ', err);
            throw err;
        }
    },
    signIn : async (req, res) => {
        const query = `SELECT * FROM user WHERE id = '${req.body.id}'`;
        try{
            return await pool.queryParam(query);
        }catch(err){
            console.err('SIGN IN ERROR : ', err);
            throw err;
        }
    },
    idCheck : async (id) => {
        const query = `SELECT name FROM user WHERE id = '${id}'`;
        try{
            const result = await pool.queryParam(query);
            return result.length != 0;
        }catch(err){
            console.log('ID CHECK ERROR : ', err);
            throw err;
        }
    }
}