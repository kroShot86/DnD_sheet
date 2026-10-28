package com.example.dnd_sheet

enum class CharState { HEALTHY, UNCONSCIOUS, DEAD }

data class CharacterAttributes(
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10,

    val currentHp: Int = 10 + (constitution * 2),
    val currentState: CharState = CharState.HEALTHY
){
    val maxHp: Int
        get() = 10 + (constitution * 2)
}