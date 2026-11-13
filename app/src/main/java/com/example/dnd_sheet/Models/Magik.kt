package com.example.dnd_sheet.Models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
enum class SpellLevel(val displayName: String) {
    CANTRIP("Заговоры"),
    LEVEL_1("1 уровень"),
    LEVEL_2("2 уровень"),
    LEVEL_3("3 уровень"),
    LEVEL_4("4 уровень"),
    LEVEL_5("5 уровень"),
    LEVEL_6("6 уровень"),
    LEVEL_7("7 уровень"),
    LEVEL_8("8 уровень"),
    LEVEL_9("9 уровень")
}

@Serializable
data class Spell(
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    val name: String = "",
    val level: SpellLevel = SpellLevel.CANTRIP,
    val description: String = "",
    val isPrepared: Boolean = false
)

@Serializable
data class SpellSlots(
    val max: Int = 0,
    val used: Int = 0
)