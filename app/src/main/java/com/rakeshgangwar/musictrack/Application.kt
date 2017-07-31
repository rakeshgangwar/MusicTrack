package com.rakeshgangwar.musictrack

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by dell on 7/30/2017.
 */

class Application : Application() {

    companion object {
        lateinit var database: FirebaseDatabase
    }

    val Context.myApp: com.rakeshgangwar.musictrack.Application
        get() = applicationContext as com.rakeshgangwar.musictrack.Application

    override fun onCreate() {
        super.onCreate()
        initializeDB()
    }

    fun initializeDB(){
        database = FirebaseDatabase.getInstance()
        Log.d("Database","DB Initialized")
    }
}