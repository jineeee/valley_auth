package com.example.auth_client.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.earlyBuddy.earlybuddy_android.data.pref.SharedPreferenceController
import com.example.auth_client.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onClick()
    }

    private fun onClick(){
        act_main_tv_admin_verify.setOnClickListener {
            val intent = Intent(this@MainActivity, EmailVerifyActivity::class.java)
            startActivity(intent)
        }

        act_main_tv_user_info.setOnClickListener {
            if(SharedPreferenceController.getAdmin(this@MainActivity)!="1"){
                Toast.makeText(this@MainActivity, "관리자만 접근할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}