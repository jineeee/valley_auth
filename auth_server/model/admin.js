const pool = require('../modules/pool');

module.exports = {
    updateAdmin: async(id) => {
        const query = `UPDATE user SET admin = 1 WHERE id = '${id}'`;
        try{
            const result = await pool.queryParam(query)
            return result.affectedRows;
        }catch(err){
            console.log("UPDATE ADMIN ERROR : ", err);
            throw err;
        }
    },
    readUsers : async() => {
        const query = "SELECT id, name, department, `rank`, admin FROM user";
        try{
            const result = await pool.queryParam(query);
            return result;
        }catch(err){
            console.log("READ USERS ERROR : ", err);
            throw err;
        }
    },
    readUser : async(id) => {
        const query = `SELECT id, name, department, \`rank\`, admin FROM user WHERE id = '${id}'`;
        try{
            const result = await pool.queryParam(query);
            return result;
        }catch(err){
            console.log("READ USERS ERROR : ", err);
            throw err;
        }
    },
    updateUserInfo : async(data) => {
        const query = `UPDATE user SET name = '${data.name}', department = '${data.department}', \`rank\` = '${data.rank}' WHERE id = '${data.id}'`;
        try{
            const result = await pool.queryParam(query);
            return result.affectedRows;
        }catch(err){
            console.log("UPDATE USER INFO ERROR : ", err);
            throw err;
        }
    }
}