package br.brlgames.sweetalchemy100.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.brlgames.sweetalchemy100.application.GlobalApp
import br.brlgames.sweetalchemy100.network.data.GameResponse
import br.brlgames.sweetalchemy100.network.repository.GameRepository
import br.brlgames.sweetalchemy100.utilities.Constant.apiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(
    val app: Application,
    val gameRepository: GameRepository) : ViewModel() {
    var clientIdLiveData = MutableLiveData<GameResponse>()

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = gameRepository.fetchClientId(GlobalApp.appCode)
                if (response.isSuccessful) {
                    val data = response.body()
                    clientIdLiveData.postValue(data)

                    Log.d("networkSuccess",response.toString())
                } else {
                    Log.d(apiResponse,"Network call failed")
                }
            } catch (e: Exception) {
               Log.d("networkResponse", e.toString())
            }
        }
    }
}