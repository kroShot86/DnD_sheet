package com.example.dnd_sheet.Ktor

import com.example.dnd_sheet.Models.SpellDto
import io.ktor.client.call.body
import io.ktor.client.request.get

object SpellApi {
    suspend fun getAllSpells(): List<SpellDto> {
        return KtorClient.client
            .get("${KtorClient.BASE_URL}all_spells.json")
            .body()
    }
}