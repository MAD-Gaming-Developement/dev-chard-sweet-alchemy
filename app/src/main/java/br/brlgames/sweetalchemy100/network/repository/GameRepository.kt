package br.brlgames.sweetalchemy100.network.repository

import br.brlgames.sweetalchemy100.network.data.GameResponse
import br.brlgames.sweetalchemy100.utilities.RetrofitHelper.apiService
import retrofit2.Response

class GameRepository {
    suspend fun fetchClientId(appId: String): Response<GameResponse> {
        return apiService.getClientId(appId)
    }
}