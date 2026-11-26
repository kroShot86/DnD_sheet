package com.example.dnd_sheet.Models

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Attack(
    val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    val name: String = "",
    val attackBonus: String = "",
    val damage: String = "",
    val damageType: String = ""
)