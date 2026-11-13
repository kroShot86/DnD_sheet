package com.example.dnd_sheet

import kotlinx.serialization.Serializable


@Serializable
enum class StatType { STR, DEX, CON, INT, WIS, CHA }

@Serializable
enum class ProficiencyLevel { NONE, PROFICIENT, EXPERTISE }

@Serializable
data class Skill(
    val name: String,
    val stat: StatType,
    val proficiency: ProficiencyLevel = ProficiencyLevel.NONE,
    val extraBonus: Int = 0

)