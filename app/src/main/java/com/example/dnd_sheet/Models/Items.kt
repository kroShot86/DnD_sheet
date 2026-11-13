package com.example.dnd_sheet.Models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class Type { ARMORS, WEAPONS, CONSUMABLES, MAGIC_ITEMS, OTHER, ALL}
@Serializable
data class Items(

    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,

    val name: String = "",
    val weight: Float = 0.0F,
    val count: Int = 0,
    val itemType: Type = Type.OTHER,
) {
    val totalWeight: Float
        get() = weight * count
}