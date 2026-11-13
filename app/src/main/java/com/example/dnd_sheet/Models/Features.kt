package com.example.dnd_sheet

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val id: Long = java.util.UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE,
    val name: String = "",
    val description: String = ""
)