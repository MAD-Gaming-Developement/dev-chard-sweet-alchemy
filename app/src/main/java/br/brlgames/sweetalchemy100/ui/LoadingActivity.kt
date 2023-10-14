package br.brlgames.sweetalchemy100.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.brlgames.sweetalchemy100.R
import br.brlgames.sweetalchemy100.application.GlobalApp
import br.brlgames.sweetalchemy100.databinding.ActivityLoadingBinding
import br.brlgames.sweetalchemy100.utilities.Constant
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpFirebaseConfig()
    }
    private fun setUpFirebaseConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.default_config)
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task: Task<Boolean> ->
            if (task.isSuccessful) {
                val updated = task.result
                Toast.makeText(this, "Able to get the config", Toast.LENGTH_SHORT).show()
                Log.d(
                    Constant.firebaseActivityTAG,
                    "Remote Config updated:$updated"
                )
            } else {
                Toast.makeText(this, "Unable to get the config", Toast.LENGTH_SHORT).show()
                Log.d(
                    Constant.firebaseActivityTAG,
                    "Unable to get Config succesfully"
                )
            }
            setConfig()
        }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                setConfig()
                Log.d(
                    Constant.firebaseActivityTAG,
                    "Updated Config:$configUpdate"
                )
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                setConfig()
                error.localizedMessage?.let {
                    Log.d(
                        Constant.firebaseActivityTAG,
                        it
                    )
                }
            }
        })
    }

    private fun setConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        GlobalApp.apiURL = remoteConfig.getString("apiURL")
        GlobalApp.gameURL = remoteConfig.getString("gameURL")
        GlobalApp.jsInterface = remoteConfig.getString("jsInterface")
        GlobalApp.policyURL = remoteConfig.getString("policyURL")
        GlobalApp.appFlyerAPI = remoteConfig.getString("appFlyerAPI")
        goToActivity()
    }

    private fun goToActivity(){
        val sharedPref = getSharedPreferences(GlobalApp.appCode, MODE_PRIVATE)
        if (sharedPref.getBoolean("runOnce", false)) {
            val webApp = Intent(this, MainActivity::class.java)
            webApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(webApp)
            finish()
        } else {
            val dataPolicy = Intent(this, ConsentActivity::class.java)
            dataPolicy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dataPolicy)
            finish()
        }
    }
}

