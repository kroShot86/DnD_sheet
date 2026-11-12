package com.example.dnd_sheet

import kotlinx.serialization.Serializable

@Serializable
enum class CharState { HEALTHY, UNCONSCIOUS, DEAD }
@Serializable
data class CharacterAttributes(

    val id: Long = java.util.UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,

    val nickname: String = "Инкогнито",
    val characterClass: String = "",
    val level: Int = 1,
    val exp: Int = 0,

    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10,

    val currentHp: Int = 10 + (constitution * 2),

    val currentState: CharState = CharState.HEALTHY,

    val inventory: List<Items> = emptyList(),

    val avatarUri: String? = null
){


    val maxHp: Int
        get() = 10 + (constitution * 2)

    val totalWeight: Float
        get() = inventory.sumOf { it.totalWeight.toDouble() }.toFloat()

}