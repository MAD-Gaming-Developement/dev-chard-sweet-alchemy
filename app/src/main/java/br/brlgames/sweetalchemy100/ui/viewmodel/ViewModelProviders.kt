package br.brlgames.sweetalchemy100.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.brlgames.sweetalchemy100.network.repository.GameRepository

class ViewModelProviders (
    val app : Application,
    val gameRepository: GameRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(app,gameRepository) as T
    }
}
