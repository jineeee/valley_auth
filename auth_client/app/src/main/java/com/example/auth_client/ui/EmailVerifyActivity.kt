package com.example.auth_client.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_email_verify.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerifyActivity : AppCompatActivity() {
    var flag = 0
    var id: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verify)

        flag = intent.getIntExtra("flag", 0)
        id = intent.getStringExtra("id")

        if (flag == 1) {
            act_email_verify_tv_title.text = "비밀번호 찾기"
        }
        act_email_verify_tv_send.setOnClickListener {
            val email = act_email_verify_et_email.text.toString()
            when {
                email.isEmpty() -> {
                    Toast.makeText(this@EmailVerifyActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
                flag == 1 -> {
                    postFindPassword(email)
                }
                else -> {
                    postEmail(email)
                }
            }
        }

    }

    private fun postEmail(email: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.emailVerify(body)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@EmailVerifyActivity, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            Toast.makeText(
                                this@EmailVerifyActivity,
                                "이메일 전송에 성공했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@EmailVerifyActivity, NumberVerifyActivity::class.java)
                            intent.putExtra("rand", response.body()!!.data)
                            startActivity(intent)
                            finish()
                        }
                        400 -> {
                            Toast.makeText(
                                this@EmailVerifyActivity,
                                "회사 이메일을 입력해주세요",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        )
    }

    private fun postFindPassword(email: String) {
        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("email", email)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.findPassword(body)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@EmailVerifyActivity, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            Toast.makeText(
                                this@EmailVerifyActivity,
                                "이메일로 전송된 임시 비밀번호를 확인해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        400 -> {
                            Toast.makeText(
                                this@EmailVerifyActivity,
                                "이메일 전송에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        )
    }

}