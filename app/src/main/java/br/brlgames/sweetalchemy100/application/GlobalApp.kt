package br.brlgames.sweetalchemy100.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp

class GlobalApp : Application() {

    companion object {
        const val appCode = "BRL002GS"
        var appFlyersId = ""
        var facebookAppId = ""
        var facebookClientToken = ""
        var apiURL = ""
        var policyURL = ""
        var gameURL = ""
        var jsInterface = ""
        var appFlyerAPI = ""
    }

    lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(appCode, Context.MODE_PRIVATE)

        FirebaseApp.initializeApp(this)
    }
}