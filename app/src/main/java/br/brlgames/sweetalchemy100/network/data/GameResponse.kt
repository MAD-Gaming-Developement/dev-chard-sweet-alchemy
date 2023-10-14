package br.brlgames.sweetalchemy100.network.data

import androidx.annotation.Keep


@Keep
data class GameResponse(
    val gameURL: String? = null,
    val policyURL: String? = null,
    val status: String? = null
)