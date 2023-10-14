package br.brlgames.sweetalchemy100.network.datasource

import br.brlgames.sweetalchemy100.network.data.GameResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GameService {

    @POST("/api/clientid")
    suspend fun getClientId(@Query("appid") appid: String): Response<GameResponse>
}