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

    val skills: List<Skill> = listOf(
        Skill("Акробатика", StatType.DEX),
        Skill("Анализ", StatType.INT),
        Skill("Атлетика", StatType.STR),
        Skill("Внимательность", StatType.WIS),
        Skill("Выживание", StatType.WIS),
        Skill("Выступление", StatType.CHA),
        Skill("Запугивание", StatType.CHA),
        Skill("История", StatType.INT),
        Skill("Ловкость рук", StatType.DEX),
        Skill("Магия", StatType.INT),
        Skill("Медицина", StatType.WIS),
        Skill("Обман", StatType.CHA),
        Skill("Природа", StatType.INT),
        Skill("Проницательность", StatType.WIS),
        Skill("Религия", StatType.INT),
        Skill("Скрытность", StatType.DEX),
        Skill("Убеждение", StatType.CHA),
        Skill("Уход за животными", StatType.WIS)
    ),

    val proficientSaves: List<StatType> = emptyList(),
    val saveExtraBonuses: Map<StatType, Int> = emptyMap(),

    val avatarUri: String? = null
){


    val maxHp: Int
        get() = 10 + (constitution * 2)

    val totalWeight: Float
        get() = inventory.sumOf { it.totalWeight.toDouble() }.toFloat()

    val proficiencyBonus: Int
        get() = when (level) {
            in 1..4 -> 2
            in 5..8 -> 3
            in 9..12 -> 4
            in 13..16 -> 5
            else -> 6
        }

}