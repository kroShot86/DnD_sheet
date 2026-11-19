package com.example.dnd_sheet.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemResponse(
    val items: List<ItemDto>
)

@Serializable
data class ItemDto(
    @SerialName("item_type") val itemType: String,
    @SerialName("name") val name: String,
    @SerialName("weight") val weight: Float,
    @SerialName("count") val count: Int = 1,

    // Поля для оружия
    @SerialName("damage") val damage: String? = null,
    @SerialName("damageType") val damageType: String? = null,

    // Поля для брони
    @SerialName("armorClass") val armorClass: Int? = null,
    @SerialName("armorType") val armorType: String? = null,

    // Поля для расходников
    @SerialName("effect") val effect: String? = null,

    // Поля для простых предметов
    @SerialName("description") val description: String? = null
)