package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.DarkStone
import com.example.dnd_sheet.GoldGlow
import com.example.dnd_sheet.GoldText
import com.example.dnd_sheet.Models.Items
import com.example.dnd_sheet.R
import com.example.dnd_sheet.Models.Type
import com.example.dnd_sheet.WoodBrown
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.formatWeight
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


@Composable
fun InventoryScreen(
    character: CharacterAttributes,
    addItem: (item: Items) -> Unit,
    updateItem: (item: Items) -> Unit,
    removeItem: (itemId: Long) -> Unit,
    changeCount: (itemId: Long, delta: Int) -> Unit,
    toBack: () -> Unit,
) {

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<Items?>(null) }
    var selectedFilter by remember { mutableStateOf(Type.ALL) }
    var itemToDelete by remember { mutableStateOf<Items?>(null) }

    Box (modifier = Modifier.clearFocusOnTap()) {
        Image(
            painter = painterResource(R.drawable.back_inventory_screen),
            contentDescription = "Инвентарь",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )



        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 30.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = toBack,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldText.copy(alpha = 0.5f))
                ) {
                    Text("↩️ Назад", color = GoldText)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = character.nickname.ifEmpty { "Безымянный" },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Вес: ${formatWeight(character.totalWeight)} / ${character.strength * 15} фнт",
                        color = when {
                            character.totalWeight <= character.strength * 15 -> GoldGlow
                            else -> Color.Red
                        },
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "🎒 Инвентарь",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GoldText,
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FilterButton("⚔️", "Оружие", isSelected = selectedFilter == Type.WEAPONS, Modifier.weight(1f)) {
                    selectedFilter = Type.WEAPONS
                }
                FilterButton("🛡️", "Броня", isSelected = selectedFilter == Type.ARMORS, Modifier.weight(1f)) {
                    selectedFilter = Type.ARMORS
                }
                FilterButton("\uD83E\uDDEA", "Расходники", isSelected = selectedFilter == Type.CONSUMABLES, Modifier.weight(1f)) {
                    selectedFilter = Type.CONSUMABLES
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FilterButton("✨", "Маг предметы", isSelected = selectedFilter == Type.MAGIC_ITEMS, Modifier.weight(1f)) {
                    selectedFilter = Type.MAGIC_ITEMS
                }
                FilterButton("\uD83D\uDCE6", "Другое", isSelected = selectedFilter == Type.OTHER, Modifier.weight(1f)) {
                    selectedFilter = Type.OTHER
                }
                FilterButton("\uD83C\uDF92", "Все предметы", isSelected = selectedFilter == Type.ALL, Modifier.weight(1f)) {
                    selectedFilter = Type.ALL
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6D4C41).copy(alpha = 0.9f),
                    contentColor = GoldText
                ),
                border = androidx.compose.foundation.BorderStroke(2.dp, GoldText.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "➕ Добавить предмет",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredInventory = when {
                selectedFilter == Type.ALL -> character.inventory
                else -> character.inventory.filter { it.itemType == selectedFilter }
            }.sortedBy { it.name }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredInventory, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkStone),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldText.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val icon = when(item.itemType) {
                                Type.WEAPONS -> "⚔️"
                                Type.ARMORS -> "🛡️"
                                Type.MAGIC_ITEMS -> "✨"
                                Type.CONSUMABLES -> "🧪"
                                Type.OTHER -> "📦"
                                else -> "📦"
                            }
                            Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldText,
                                    maxLines = 1
                                )
                                Text(
                                    text = "⚖️ ${formatWeight(item.weight)} фнт",
                                    fontSize = 11.sp,
                                    color = Color.LightGray
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { changeCount(item.id, -1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Text("−", fontSize = 18.sp, color = Color.LightGray)
                                    }

                                    Text(
                                        text = "${item.count}",
                                        fontSize = 14.sp,
                                        color = GoldText,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )

                                    IconButton(
                                        onClick = { changeCount(item.id, 1) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Text("+", fontSize = 18.sp, color = GoldText)
                                    }
                                }


                                Text(
                                    text = "${formatWeight(item.totalWeight)} фнт",
                                    fontSize = 14.sp,
                                    color = GoldText.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(end = 5.dp)
                                )
                            }

                            IconButton(
                                onClick = { itemToEdit = item },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("✏️", fontSize = 20.sp)
                            }

                            IconButton(
                                onClick = { itemToDelete = item },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("🗑️", fontSize = 20.sp)
                            }
                        }
                    }
                }

            }
        }

    }
    if (showAddDialog) {
        ItemDialog(
            itemToEdit = null,
            onDismiss = { showAddDialog = false },
            onSaveItem = { newItem -> addItem(newItem) }
        )
    }

    if (itemToEdit != null) {
        ItemDialog(
            itemToEdit = itemToEdit,
            onDismiss = { itemToEdit = null },
            onSaveItem = { updatedItem ->
                updateItem(updatedItem)
                itemToEdit = null
            }
        )
    }

    if (itemToDelete != null) {
        DeleteItemDialog(
            itemToDelete = itemToDelete!!,
            onDismiss = { itemToDelete = null },
            onConfirm = { itemId -> removeItem(itemId) }
        )
    }
}


@Composable
fun FilterButton(icon: String, text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) GoldText.copy(alpha = 0.5f) else WoodBrown.copy(alpha = 0.8f),
            contentColor = if (isSelected) Color.White else GoldText
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, GoldText.copy(alpha = 0.4f)),
        contentPadding = PaddingValues(2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 20.sp)
            Text(text, fontSize = 12.sp, color = GoldText)
        }
    }
}

@Preview(showBackground = true, name = "Инвентарь")
@Composable
fun PreviewInventoryScreen() {
    DnD_sheetTheme {
        InventoryScreen(
            character = CharacterAttributes(
                nickname = "Арагорн",
                inventory = listOf(
                    Items(id = 1, name = "Длинный меч", weight = 3f, count = 1, itemType = Type.WEAPONS),
                    Items(id = 2, name = "Кольчуга", weight = 12f, count = 1, itemType = Type.ARMORS),
                    Items(id = 3, name = "Зелье лечения", weight = 0.5f, count = 3, itemType = Type.CONSUMABLES)
                )
            ),
            addItem = {},
            updateItem = {},
            removeItem = {},
            changeCount = { _, _ -> },
            toBack = {}
        )
    }
}