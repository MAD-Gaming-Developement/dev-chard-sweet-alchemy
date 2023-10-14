package br.brlgames.sweetalchemy100.utilities




import br.brlgames.sweetalchemy100.application.GlobalApp
import br.brlgames.sweetalchemy100.network.datasource.GameService
import br.brlgames.sweetalchemy100.utilities.Constant.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(GlobalApp.apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService: GameService by lazy {
        retrofit.create(GameService::class.java)
    }
}