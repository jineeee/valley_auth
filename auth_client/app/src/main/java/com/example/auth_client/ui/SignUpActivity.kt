package com.example.auth_client.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        act_sign_up_tv_sign_up.setOnClickListener {
            if (pwCheck(act_sign_up_et_pw.text.toString(), act_sign_up_et_pw_check.text.toString())) {
                postSignUp()
            } else {
                Toast.makeText(this@SignUpActivity, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pwCheck(pw: String, pwCheck: String): Boolean {
        return pw == pwCheck
    }

    private fun postSignUp() {
        val id = act_sign_up_et_id.text
        val pw = act_sign_up_et_pw.text
        val name = act_sign_up_et_name.text
        val department = act_sign_up_et_department.text
        val rank = act_sign_up_et_rank.text

        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("pw", pw)
        jsonObject.put("name", name)
        jsonObject.put("department", department)
        jsonObject.put("rank", rank)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.postSignUp(body)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@SignUpActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        204 -> {
                            Toast.makeText(this@SignUpActivity, "값을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                        400 -> {
                            Toast.makeText(this@SignUpActivity, "중복된 아이디 입니다.", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            Toast.makeText(this@SignUpActivity, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                            intent.putExtra("id", act_sign_up_et_id.text.toString())
                            intent.putExtra("pw", act_sign_up_et_pw.text.toString())
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }

            }
        )
    }
}