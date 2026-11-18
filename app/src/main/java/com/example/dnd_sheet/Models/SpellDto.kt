package com.example.dnd_sheet.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpellDto(
    @SerialName("name_ru") val nameRu: String,
    @SerialName("name_en") val nameEn: String,
    @SerialName("level") val level: String,
    @SerialName("school") val school: String,
    @SerialName("time") val time: String,
    @SerialName("distans") val distance: String,
    @SerialName("components") val components: String,
    @SerialName("time_doing") val duration: String,
    @SerialName("class") val characterClass: String? = null,
    @SerialName("text") val text: String
)