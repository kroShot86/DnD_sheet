package com.example.dnd_sheet.Ktor

import com.example.dnd_sheet.Models.ItemDto
import com.example.dnd_sheet.Models.ItemResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

object ItemApi {
    suspend fun getAllItems(): List<ItemDto> {
        val response: ItemResponse = KtorClient.client
            .get("${KtorClient.BASE_URL}all_items.json")
            .body()
        return response.items
    }
}