package com.example.dnd_sheet.Models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import java.util.UUID

@Serializable
enum class Type { ARMORS, WEAPONS, CONSUMABLES, MAGIC_ITEMS, OTHER, ALL }

// Базовый sealed класс
@Serializable
@JsonClassDiscriminator("item_type")
sealed class Item {
    abstract val id: Long
    abstract val name: String
    abstract val weight: Float
    abstract val count: Int
    abstract val itemType: Type

    val totalWeight: Float
        get() = weight * count
}

// Оружие
@Serializable
@SerialName("weapon")
data class Weapon(
    override val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    override val name: String = "",
    override val weight: Float = 0.0F,
    override val count: Int = 1,
    override val itemType: Type = Type.WEAPONS,
    val damage: String = "1к6",
    val damageType: String = ""
) : Item()

// Броня
@Serializable
@SerialName("armor")
data class Armor(
    override val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    override val name: String = "",
    override val weight: Float = 0.0F,
    override val count: Int = 1,
    override val itemType: Type = Type.ARMORS,
    val armorClass: Int = 10,
    val armorType: String = ""
) : Item()

// Расходники
@Serializable
@SerialName("consumable")
data class Consumable(
    override val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    override val name: String = "",
    override val weight: Float = 0.0F,
    override val count: Int = 1,
    override val itemType: Type = Type.CONSUMABLES,
    val effect: String = ""
) : Item()

// Простые предметы (веревка, факел и тд)
@Serializable
@SerialName("simple")
data class SimpleItem(
    override val id: Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    override val name: String = "",
    override val weight: Float = 0.0F,
    override val count: Int = 1,
    override val itemType: Type = Type.OTHER,
    val description: String = ""
) : Item()