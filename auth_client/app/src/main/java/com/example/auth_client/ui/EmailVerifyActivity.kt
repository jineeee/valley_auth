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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verify)

        act_email_verify_tv_send.setOnClickListener {
            postEmail()
        }
    }

    private fun postEmail() {
        val jsonObject = JSONObject()
        jsonObject.put("email", act_email_verify_et_email.text)

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
                            val intent = Intent(this@EmailVerifyActivity, NumberVerifyActivity::class.java)
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

}