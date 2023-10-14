package br.brlgames.sweetalchemy100.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import br.brlgames.sweetalchemy100.application.GlobalApp
import br.brlgames.sweetalchemy100.databinding.ActivityMainBinding
import br.brlgames.sweetalchemy100.network.repository.GameRepository
import br.brlgames.sweetalchemy100.ui.viewmodel.GameViewModel
import br.brlgames.sweetalchemy100.ui.viewmodel.ViewModelProviders

class MainActivity : AppCompatActivity() {
    private var bckExit = false

    var appSharedPref: SharedPreferences? = null

    private lateinit var binding : ActivityMainBinding

    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val gameRepository = GameRepository()
        val viewModelProviderFactory = ViewModelProviders(application,gameRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory)[GameViewModel::class.java]

        appSharedPref = getSharedPreferences(GlobalApp.appCode, MODE_PRIVATE)
        showWV()
        setContentView(binding.root)

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showWV(){
        viewModel.fetchData()
        val webView = binding.wvApiContent
        webView.webChromeClient = WebChromeClient()
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.settings.allowFileAccess = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.setSupportMultipleWindows(true)
        webView.loadUrl(GlobalApp.gameURL)
    }

    override fun onBackPressed() {
        if (bckExit) {
            super.finishAffinity()
            return
        }
        this.bckExit = true
        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ bckExit = false }, 2000)
    }
}