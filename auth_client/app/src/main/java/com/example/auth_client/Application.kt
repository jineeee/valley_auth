package com.example.auth_client

import android.app.Application
import android.content.Context

class Application : Application() {

    init{
        instance = this
    }

    companion object {
        private var instance: Application? = null
        fun getApplicationContext() : Context {
            return instance!!.applicationContext
        }
    }

}