package com.example.auth_client.presentation

import NetworkServiceImpl
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.auth_client.R
import com.example.auth_client.data.model.SignInResponse
import com.example.auth_client.data.pref.SharedPreferenceController
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        onClick()
    }

    private fun onClick() {
        act_sign_in_tv_sign_in.setOnClickListener {
            if (act_sign_in_et_id.text.isEmpty() || act_sign_in_et_pw.text.isEmpty()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                postSignIn()
            }
        }

        act_sign_in_tv_sign_up.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        act_sign_in_tv_find_password.setOnClickListener {
            val intent = Intent(this, IdActivity::class.java)
            startActivity(intent)
        }
    }

    private fun postSignIn() {
        val id = act_sign_in_et_id.text.toString()
        val pw = act_sign_in_et_pw.text.toString()
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("pw", pw)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<SignInResponse> = NetworkServiceImpl.SERVICE.postSignIn(body)
        call.enqueue(
            object : Callback<SignInResponse> {
                override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                    Toast.makeText(this@SignInActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<SignInResponse>,
                    response: Response<SignInResponse>
                ) {
                    when (response.code()) {
                        400 -> {
                            Toast.makeText(this@SignInActivity, "아이디 또는 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            Toast.makeText(this@SignInActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            SharedPreferenceController.setAuthorization(applicationContext,response.body()!!.data.accessToken)
                            SharedPreferenceController.setAdmin(applicationContext,response.body()!!.data.admin)
                            SharedPreferenceController.setRefreshAuthorization(applicationContext, response.body()!!.data.refreshToken)

                            Toast.makeText(this@SignInActivity, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }

            }
        )
    }
}