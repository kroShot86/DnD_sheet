package com.example.dnd_sheet

import androidx.compose.ui.text.font.FontWeight

enum class Type { ARMOR, WEAPON, AMMUNITION, POTION,  INGREDIENTS, OTHER }

data class Items(

    val id: Long = System.currentTimeMillis(),

    val name: String = "",
    val weight: Float = 0.0F,
    val count: Int = 0,
    val itemType: Type = Type.OTHER,
) {
    val totalWeight: Float
        get() = weight * count
}