package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.dnd_sheet.Models.Item
import com.example.dnd_sheet.Models.Weapon
import com.example.dnd_sheet.Models.Armor
import com.example.dnd_sheet.Models.Consumable
import com.example.dnd_sheet.Models.SimpleItem
import com.example.dnd_sheet.R
import com.example.dnd_sheet.Models.Type
import com.example.dnd_sheet.WoodBrown
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.formatWeight
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun InventoryScreen(
    character: CharacterAttributes,
    addItem: (item: Item) -> Unit,
    updateItem: (item: Item) -> Unit,
    removeItem: (itemId: Long) -> Unit,
    changeCount: (itemId: Long, delta: Int) -> Unit,
    toBack: () -> Unit,
) {

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<Item?>(null) }
    var selectedFilter by remember { mutableStateOf(Type.ALL) }
    var itemToDelete by remember { mutableStateOf<Item?>(null) }

    val itemSearchViewModel: ItemSearchViewModel = viewModel()

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
                            val icon = when(item) {
                                is Weapon -> "⚔️"
                                is Armor -> "🛡️"
                                is Consumable -> "🧪"
                                else -> "📦"
                            }
                            Text(icon, fontSize = 22.sp, modifier = Modifier.padding(end = 10.dp))

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldText,
                                    maxLines = 1,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                                )

                                when(item) {
                                    is Weapon -> Text(
                                        text = "Урон: ${item.damage} (${item.damageType})",
                                        fontSize = 11.sp,
                                        color = Color(0xFFFF5252),
                                        lineHeight = 13.sp,
                                        maxLines = 4,
                                    )
                                    is Armor -> Text(
                                        text = "КД: ${item.armorClass} (${item.armorType})",
                                        fontSize = 11.sp,
                                        color = Color(0xFF00E5FF),
                                        lineHeight = 13.sp,
                                        maxLines = 4,
                                    )
                                    is Consumable -> Text(
                                        text = "Эффект: ${item.effect}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF69F0AE),
                                        lineHeight = 13.sp,
                                        maxLines = 4,
                                    )
                                    is SimpleItem -> if (item.description.isNotEmpty()) {
                                        Text(
                                            text = item.description,
                                            fontSize = 11.sp,
                                            color = Color.LightGray,
                                            lineHeight = 13.sp,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 4,
                                        )
                                    }
                                    else -> {}
                                }

                                Text(
                                    text = "⚖️ ${formatWeight(item.weight)} фнт",
                                    fontSize = 11.sp,
                                    color = Color.LightGray,
                                    lineHeight = 13.sp
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { changeCount(item.id, -1) }, modifier = Modifier.size(28.dp)) {
                                        Text("−", fontSize = 18.sp, color = Color.LightGray)
                                    }
                                    Text(
                                        text = "${item.count}",
                                        fontSize = 14.sp,
                                        color = GoldText,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    IconButton(onClick = { changeCount(item.id, 1) }, modifier = Modifier.size(28.dp)) {
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
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Редактировать",
                                    tint = GoldText.copy(alpha = 0.7f)
                                )
                            }

                            IconButton(
                                onClick = { itemToDelete = item },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    tint = Color(0xFFFF5252).copy(alpha = 0.8f)
                                )
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
            itemSearchViewModel = itemSearchViewModel,
            onDismiss = { showAddDialog = false },
            onSaveItem = { newItem -> addItem(newItem) }
        )
    }

    if (itemToEdit != null) {
        ItemDialog(
            itemToEdit = itemToEdit,
            itemSearchViewModel = itemSearchViewModel,
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
                    Weapon(id = 1, name = "Длинный меч", weight = 3f, count = 1, damage = "1к8", damageType = "Рубящий"),
                    Armor(id = 2, name = "Кольчуга", weight = 12f, count = 1, armorClass = 16, armorType = "Средняя"),
                    Consumable(id = 3, name = "Зелье лечения", weight = 0.5f, count = 3, effect = "Восстанавливает 2к4+2 хита")
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