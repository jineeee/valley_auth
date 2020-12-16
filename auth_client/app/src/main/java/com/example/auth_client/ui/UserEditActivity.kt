package com.example.auth_client.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.auth_client.R
import com.example.auth_client.data.model.DefaultResponse
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_user_edit.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEditActivity : AppCompatActivity() {
    var id: String? = null
    var name: String? = null
    var department: String? = null
    var rank: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_edit)

        id = intent.getStringExtra("id")
        name = intent.getStringExtra("name")
        department = intent.getStringExtra("department")
        rank = intent.getStringExtra("rank")
        setText()

        act_user_edit_tv_send.setOnClickListener {
            postSignUp()
        }
    }

    private fun setText(){
        act_user_edit_et_name.hint = name
        act_user_edit_et_department.hint = department
        act_user_edit_et_rank.hint = rank
    }

    private fun postSignUp() {
        val name = act_user_edit_et_name.text
        val department = act_user_edit_et_department.text
        val rank = act_user_edit_et_rank.text

        val jsonObject = JSONObject()
        jsonObject.put("id", id)
        jsonObject.put("name", name)
        jsonObject.put("department", department)
        jsonObject.put("rank", rank)

        val body = JsonParser().parse(jsonObject.toString()) as JsonObject
        val call: Call<DefaultResponse> = NetworkServiceImpl.SERVICE.updateUserInfo(body)
        call.enqueue(
            object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(this@UserEditActivity, "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    when (response.code()) {
                        204 -> {
                            Toast.makeText(this@UserEditActivity, "값을 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            Toast.makeText(this@UserEditActivity, "정보 수정에 성공했습니다", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        600 -> {
                            Toast.makeText(this@UserEditActivity, "DB 에러", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@UserEditActivity, "정보 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        )
    }
}