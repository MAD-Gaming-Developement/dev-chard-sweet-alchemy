package br.brlgames.sweetalchemy100.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import br.brlgames.sweetalchemy100.R
import br.brlgames.sweetalchemy100.application.GlobalApp
import br.brlgames.sweetalchemy100.databinding.ActivityConsentBinding
import br.brlgames.sweetalchemy100.utilities.PermissionHelper

class ConsentActivity : AppCompatActivity() {

    private lateinit var permissionHelper: PermissionHelper
     private lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: ActivityConsentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        displayDataPolicy()

        permissionHelper = PermissionHelper(this)
        sharedPreferences = getSharedPreferences(GlobalApp.appCode, Context.MODE_PRIVATE)


        // Check and request permissions when needed
        if (!permissionHelper.checkPermissions()) {
            permissionHelper.requestPermissions()
        }
    }

    private fun displayDataPolicy(){
        binding.wvPolicyContent.webViewClient = WebViewClient()
        binding.wvPolicyContent.loadUrl(GlobalApp.policyURL)

        binding.btnAccept.setOnClickListener {

            if (!permissionHelper.checkPermissions()) {
                permissionHelper.requestPermissions()
            }
            else {
                showUserConsent()
            }
    }

        binding.btnDecline.setOnClickListener {
            val editSharedPref = sharedPreferences.edit()
            editSharedPref.putBoolean("runOnce", false)
            editSharedPref.apply()
            editSharedPref.commit()
            finishAffinity()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionHelper.PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                updateSharedPreferences(
                    location = true,
                    camera = true,
                    media = true,
                    runOnce = true
                )
            } else {
                // Permissions not granted
                updateSharedPreferences(
                    location = false,
                    camera = false,
                    media = false,
                    runOnce = false
                )
            }
        }
        showUserConsent()
    }

    private fun updateSharedPreferences(
        location: Boolean,
        camera: Boolean,
        media: Boolean,
        runOnce: Boolean
    ) {
        val sharedPreferences = getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        val editSharedPref = sharedPreferences.edit()

        editSharedPref.putBoolean("locationGranted", location)
        editSharedPref.putBoolean("cameraGranted", camera)
        editSharedPref.putBoolean("mediaGranted", media)
        editSharedPref.putBoolean("runOnce", runOnce)
        editSharedPref.apply()
        showUserConsent()
    }

    private fun showUserConsent() {
        val editSharedPref = sharedPreferences.edit()
        val permitSendData = sharedPreferences.getBoolean("permitSendData", false)
        if (!permitSendData) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("User Data Consent")
                .setMessage(getString(R.string.data_consent))
                .setPositiveButton(
                    "Accept"
                ) { allowDialog, _ ->
                    editSharedPref.putBoolean("permitSendData", true)
                    editSharedPref.apply()
                    allowDialog.dismiss()
                    startApp()
                }
                .setNegativeButton(
                    "Don't send Data"
                ) { dialog, _ ->
                    editSharedPref.putBoolean("permitSendData", false)
                    editSharedPref.apply()
                    dialog.dismiss()
                }
            builder.show()
        } else {
            startApp()
        }
    }
    private fun startApp() {
        val splashIntent = Intent(this, MainActivity::class.java)
        splashIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(splashIntent)
        finish()
    }
}