package com.example.auth_client.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.example.auth_client.data.pref.SharedPreferenceController
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_change_pw.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePwActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pw)

        act_change_pw_tv_send.setOnClickListener {
            val newPw = act_change_pw_et_new_pw.text.toString()
            val newPwCheck = act_change_pw_et_new_pw_check.text.toString()
            if(newPw != newPwCheck){
                Toast.makeText(this@ChangePwActivity, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }else{
                postChangePw(newPw)
            }
        }
    }

    private fun postChangePw(newPw: String) {
        val pw = act_change_pw_et_pw.text.toString()
        val jsonObject = JSONObject()
        jsonObject.put("pw", pw)
        jsonObject.put("newPw", newPw)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.changePassword(body)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@ChangePwActivity, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        400 -> {
                            Toast.makeText(this@ChangePwActivity, "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            Toast.makeText(this@ChangePwActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            Toast.makeText(this@ChangePwActivity, "비밀번호 변경에 성공했습니다.", Toast.LENGTH_SHORT).show()
                            SharedPreferenceController.deleteAdmin(applicationContext)
                            SharedPreferenceController.deleteAuthorization(applicationContext)
                            finishAffinity()
                            val intent = Intent(this@ChangePwActivity, SignInActivity::class.java)
                            startActivity(intent)
                        }
                    }

                }

            }
        )
    }
}