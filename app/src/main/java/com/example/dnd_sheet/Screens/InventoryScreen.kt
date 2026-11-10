package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.CharacterViewModel
import com.example.dnd_sheet.Items
import com.example.dnd_sheet.R
import com.example.dnd_sheet.Type
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


val DarkStone = Color(0xE6151010)
val GoldText = Color(0xFFFFD700)
val WoodBrown = Color(0xFF4E342E)

@Composable
fun InventoryScreen(
    character: CharacterAttributes,
    addItem: (item: Items) -> Unit,
    removeItem: (itemId: Long) -> Unit,
    filterItems: (typeItems: Type) -> Unit,
    toBack: () -> Unit,
) {

    var showAddDialog by remember { mutableStateOf(false) }

    Box {
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
                        text = "Вес: ${character.totalWeight} кг",
                        color = GoldText,
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
                FilterButton("⚔️", "Оружие", Modifier.weight(1f)) {
                    filterItems(Type.ARMORS)
                }
                FilterButton("🛡️", "Броня", Modifier.weight(1f)) {
                    filterItems(Type.WEAPONS)
                }
                FilterButton("\uD83E\uDDEA", "Расходники", Modifier.weight(1f)) {
                    filterItems(Type.CONSUMABLES)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FilterButton("✨", "Маг предметы", Modifier.weight(1f)) {
                    filterItems(Type.MAGIC_ITEMS)
                }
                FilterButton("\uD83D\uDCE6", "Другое", Modifier.weight(1f)) {
                    filterItems(Type.OTHER)
                }
                FilterButton("\uD83C\uDF92", "Все предметы", Modifier.weight(1f)) {
                    filterItems(Type.ALL)
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
                character.typeItems == Type.ALL -> character.inventory
                else -> character.inventory.filter { it.itemType == character.typeItems }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredInventory, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkStone),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldText.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                val icon = when(item.itemType) {
                                    Type.WEAPONS -> "⚔️"
                                    Type.ARMORS -> "🛡️"
                                    Type.MAGIC_ITEMS -> "✨"
                                    Type.CONSUMABLES -> "\uD83E\uDDEA"
                                    Type.OTHER -> "📦"
                                    Type.ALL -> ""
                                }
                                Text(icon, fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))

                                Column {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium, color = Color.White)
                                    Text("Тип: ${item.itemType}", color = Color.Gray, fontSize = 12.sp)
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("${item.weight} x ${item.count}", color = GoldText, fontWeight = FontWeight.Bold)
                                Text("= ${item.totalWeight} кг", color = Color.LightGray, fontSize = 12.sp)

                                IconButton(onClick = { removeItem(item.id) }) {
                                    Text("🗑️", fontSize = 20.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (showAddDialog) {
        AddItemDialog(
            onDismiss = { showAddDialog = false },
            onAddItem = { newItem ->
                addItem(newItem)
            }
        )
    }
}


@Composable
fun FilterButton(icon: String, text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = WoodBrown.copy(alpha = 0.8f)),
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
            removeItem = {},
            filterItems = {},
            toBack = {}
        )
    }
}