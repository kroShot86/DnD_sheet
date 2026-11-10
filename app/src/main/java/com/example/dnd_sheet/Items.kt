package com.example.dnd_sheet

import kotlinx.serialization.Serializable

@Serializable
enum class Type { ARMOR, WEAPON, AMMUNITION, POTION,  INGREDIENTS, OTHER }
@Serializable
data class Items(

    val id: Long = java.util.UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,

    val name: String = "",
    val weight: Float = 0.0F,
    val count: Int = 0,
    val itemType: Type = Type.OTHER,
) {
    val totalWeight: Float
        get() = weight * count
}