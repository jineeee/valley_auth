package com.example.auth_client.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.pref.SharedPreferenceController
import kotlinx.android.synthetic.main.activity_number_verify.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NumberVerifyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_number_verify)

        val rand = intent.getStringExtra("rand")

        act_number_verify_tv_send.setOnClickListener {
            if (act_number_verify_et_number.text.toString() == rand) {
                postAdmin()
            }else {
                Toast.makeText(this@NumberVerifyActivity, "인증번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postAdmin() {
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.putAdmin()
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@NumberVerifyActivity, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            SharedPreferenceController.setAdmin(applicationContext, "1")
                            Toast.makeText(
                                this@NumberVerifyActivity,
                                "인증에 성공했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        400 -> {
                            Toast.makeText(
                                this@NumberVerifyActivity,
                                "인증에 실패했습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            }
        )
    }
}