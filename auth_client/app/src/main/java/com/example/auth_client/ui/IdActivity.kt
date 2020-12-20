package com.example.auth_client.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.auth_client.R
import kotlinx.android.synthetic.main.activity_id.*

class IdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id)
    }

    fun setClick(){
        act_id_tv_confirm.setOnClickListener {
            val intent = Intent(this@IdActivity, EmailVerifyActivity::class.java)
            intent.putExtra("flag", 1)
            startActivity(intent)
        }
    }
}