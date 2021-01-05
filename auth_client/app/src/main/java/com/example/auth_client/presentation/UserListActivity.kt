package com.example.auth_client.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.auth_client.R
import com.example.auth_client.data.model.UserListResponse
import kotlinx.android.synthetic.main.activity_user_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        initRv()
    }

    override fun onResume() {
        super.onResume()
        getUserList()
    }

    private fun initRv(){
        userListAdapter = UserListAdapter(this)
        act_user_list_rv.adapter = userListAdapter
        act_user_list_rv.layoutManager = LinearLayoutManager(this)
    }

    private fun getUserList(){
        val call: Call<UserListResponse> = NetworkServiceImpl.SERVICE.getUserList()
        call.enqueue(
            object : Callback<UserListResponse> {
                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    Toast.makeText(this@UserListActivity, "네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
                }
                override fun onResponse(
                    call: Call<UserListResponse>,
                    response: Response<UserListResponse>
                ) {
                    when (response.code()) {
                        400 -> {
                            Toast.makeText(this@UserListActivity, "조회에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                        200 -> {
                            userListAdapter.data = response.body()!!.data
                            userListAdapter.notifyDataSetChanged()
                        }
                    }
                }

            }
        )
    }
}