package com.example.auth_client.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.earlyBuddy.earlybuddy_android.data.pref.SharedPreferenceController
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.model.SignInResponse
import kotlinx.android.synthetic.main.activity_id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id)
        setClick()
    }

    private fun setClick(){
        act_id_tv_confirm.setOnClickListener {
            val id = act_id_et_id.text.toString()
            if(id.isEmpty()) {
                Toast.makeText(this@IdActivity, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }else{
                getUserVerify(id)
            }
        }
    }

    private fun getUserVerify(id: String){
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.userVerify(id)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@IdActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        400 -> {
                            Toast.makeText(this@IdActivity, "아이디 또는 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            val intent = Intent(this@IdActivity, EmailVerifyActivity::class.java)
                            intent.putExtra("flag", 1)
                            intent.putExtra("id", id)
                            startActivity(intent)
                            finish()
                        }
                    }

                }

            }
        )
    }
}