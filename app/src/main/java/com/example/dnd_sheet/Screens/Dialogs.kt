package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.GoldText
import com.example.dnd_sheet.Models.Items
import com.example.dnd_sheet.Models.Type
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.dnd_sheet.Feature
import com.example.dnd_sheet.Models.Spell
import com.example.dnd_sheet.Models.SpellLevel
import com.example.dnd_sheet.TextCyan
import com.example.dnd_sheet.clearFocusOnTap


//MainScreen диалоги
@Composable
fun DeleteCharacterDialog(
    characterToDelete: CharacterAttributes,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6151010),
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⚠️", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Удалить героя?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        },
        text = {
            Text(
                text = "Вы уверены, что хотите навсегда удалить ${characterToDelete.nickname}? Это действие нельзя отменить.",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(characterToDelete.id)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("Удалить", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
            ) {
                Text("Отмена")
            }
        }
    )
}

//CharacterScreen диалоги
@Composable
fun NumberInputDialog(
    title: String,
    themeColor: Color,
    confirmButtonColor: Color,
    confirmButtonTextColor: Color,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6151010),
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                // Кнопка закрытия (крестик)
                IconButton(onClick = onDismiss) {
                    Text("✖", color = Color.Gray, fontSize = 20.sp)
                }
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.CenterEnd) {
                            Text(
                                text = if (amount.isEmpty()) "0" else amount,
                                color = if (amount.isEmpty()) Color.Gray else Color.White,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.End,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Кнопка Backspace
                    Button(
                        onClick = {
                            if (amount.isNotEmpty()) {
                                amount = amount.dropLast(1)
                            }
                        },
                        modifier = Modifier.height(80.dp).width(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4E342E),
                            contentColor = themeColor
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("⌫", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }

                val buttons = listOf(
                    listOf("7", "8", "9"),
                    listOf("4", "5", "6"),
                    listOf("1", "2", "3"),
                    listOf("C", "0", "OK")
                )

                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { label ->
                            Button(
                                onClick = {
                                    when (label) {
                                        "C" -> amount = ""
                                        "OK" -> {
                                            val value = amount.toIntOrNull() ?: 0
                                            if (value > 0) {
                                                onConfirm(value)
                                                onDismiss()
                                            }
                                        }
                                        else -> {
                                            if (amount.length < 4) {
                                                amount += label
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f).height(60.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = when (label) {
                                        "OK" -> confirmButtonColor
                                        "C" -> Color(0xFF4E342E)
                                        else -> Color(0xFF2A1B1B)
                                    },
                                    contentColor = when (label) {
                                        "OK" -> confirmButtonTextColor
                                        "C" -> themeColor
                                        else -> Color.White
                                    }
                                ),
                                border = if (label != "OK" && label != "C") BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)) else null
                            ) {
                                Text(
                                    text = label,
                                    fontSize = if (label == "OK") 16.sp else 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}


//InventoryScreen диалоги
@Composable
fun ItemDialog(
    itemToEdit: Items? = null,
    onDismiss: () -> Unit,
    onSaveItem: (Items) -> Unit
) {
    var newName by remember(itemToEdit) { mutableStateOf(itemToEdit?.name ?: "") }
    var newWeight by remember(itemToEdit) { mutableStateOf(itemToEdit?.weight?.toString() ?: "") }
    var newCount by remember(itemToEdit) { mutableStateOf(itemToEdit?.count?.toString() ?: "1") }
    var newType by remember(itemToEdit) { mutableStateOf(itemToEdit?.itemType ?: Type.WEAPONS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6151010),
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = if (itemToEdit == null) "Новый предмет" else "Редактировать предмет",
                color = GoldText,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        },
        modifier = Modifier.clearFocusOnTap(),
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Название") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFFD700),
                        unfocusedBorderColor = Color(0xFFFFD700).copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFFFFD700),
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newWeight,
                    onValueChange = { newWeight = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Вес (фнт)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFFD700),
                        unfocusedBorderColor = Color(0xFFFFD700).copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFFFFD700),
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = newCount,
                    onValueChange = { newCount = it.filter { char -> char.isDigit() } },
                    label = { Text("Количество") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFFFD700),
                        unfocusedBorderColor = Color(0xFFFFD700).copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFFFFD700),
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Тип:", color = Color(0xFFFFD700), fontSize = 14.sp)
                var expanded by remember { mutableStateOf(false) }


                fun getTypeName(type: Type): String {
                    return when (type) {
                        Type.WEAPONS -> "Оружие"
                        Type.ARMORS -> "Броня"
                        Type.CONSUMABLES -> "Расходники"
                        Type.MAGIC_ITEMS -> "Магические предметы"
                        Type.OTHER -> "Другое"
                        else -> "Неизвестно"
                    }
                }

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, GoldText.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldText)
                    ) {
                        Text(
                            text = "Тип: ${getTypeName(newType)}",
                            modifier = Modifier.weight(1f)
                        )
                        Text("▼")
                    }


                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF2A1B1B))
                    ) {
                        listOf(
                            Type.WEAPONS,
                            Type.ARMORS,
                            Type.CONSUMABLES,
                            Type.MAGIC_ITEMS,
                            Type.OTHER
                        ).forEach { type ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = getTypeName(type),
                                        color = if (newType == type) GoldText else Color.White
                                    )
                                },
                                onClick = {
                                    newType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val weight = newWeight.toFloatOrNull() ?: 0f
                    val count = newCount.toIntOrNull() ?: 1
                    if (newName.isNotBlank()) {
                        val item = itemToEdit?.copy(
                            name = newName,
                            weight = weight,
                            count = count,
                            itemType = newType
                        ) ?: Items(name = newName, weight = weight, count = count, itemType = newType)
                        onSaveItem(item)
                        onDismiss()
                    }
                },
                enabled = newName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = GoldText, contentColor = Color.Black)
            ) {
                Text(if (itemToEdit == null) "Добавить" else "Сохранить", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Отмена", color = Color.LightGray)
            }
        }
    )
}



@Composable
fun DeleteItemDialog(
    itemToDelete: Items,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6151010),
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⚠️", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Удалить предмет?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
        },
        text = {
            Text(
                text = "Вы уверены, что хотите удалить «${itemToDelete.name}»?",
                color = Color.LightGray,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(itemToDelete.id)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Text("Удалить", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
            ) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun SpellDialog(
    spellToEdit: Spell? = null,
    onDismiss: () -> Unit,
    onSave: (Spell) -> Unit
) {
    var name by remember(spellToEdit) { mutableStateOf(spellToEdit?.name ?: "") }
    var level by remember(spellToEdit) { mutableStateOf(spellToEdit?.level ?: SpellLevel.CANTRIP) }
    var description by remember(spellToEdit) { mutableStateOf(spellToEdit?.description ?: "") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6101520),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clearFocusOnTap(),
        title = {
            Text(
                text = if (spellToEdit == null) "✨ Новое заклинание" else "✏️ Редактировать",
                color = TextCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TextCyan,
                        unfocusedBorderColor = TextCyan.copy(alpha = 0.3f),
                        focusedLabelColor = TextCyan,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, TextCyan.copy(alpha = 0.5f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextCyan)
                    ) {
                        Text(text = "Уровень: ${level.displayName}", modifier = Modifier.weight(1f))
                        Text("▼")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color(0xFF101820))
                    ) {
                        SpellLevel.entries.forEach { lvl ->
                            DropdownMenuItem(
                                text = { Text(lvl.displayName, color = if (level == lvl) TextCyan else Color.White) },
                                onClick = {
                                    level = lvl
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание / Эффект") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = TextCyan,
                        unfocusedBorderColor = TextCyan.copy(alpha = 0.3f),
                        focusedLabelColor = TextCyan,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val spell = spellToEdit?.copy(
                        name = name, level = level, description = description
                    ) ?: Spell(name = name, level = level, description = description)
                    onSave(spell)
                    onDismiss()
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = TextCyan, contentColor = Color.Black)
            ) {
                Text(if (spellToEdit == null) "Добавить" else "Сохранить", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray)
            ) {
                Text("Отмена")
            }
        }
    )
}

@Composable
fun SpellSlotDialog(
    currentMax: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var amount by remember { mutableStateOf(if (currentMax == 0) "" else currentMax.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6101520),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.clearFocusOnTap(),
        title = { Text("🔮 Настроить ячейки", color = TextCyan, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.length <= 2 && it.all { char -> char.isDigit() }) amount = it },
                label = { Text("Максимум ячеек") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = TextCyan,
                    unfocusedBorderColor = TextCyan.copy(alpha = 0.3f),
                    focusedLabelColor = TextCyan,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(amount.toIntOrNull() ?: 0)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = TextCyan, contentColor = Color.Black)
            ) {
                Text("Сохранить", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, border = BorderStroke(1.dp, Color.Gray)) {
                Text("Отмена", color = Color.LightGray)
            }
        }
    )
}


@Composable
fun FeatureDialog(
    featureToEdit: Feature? = null,
    onDismiss: () -> Unit,
    onSave: (Feature) -> Unit
) {
    var name by remember(featureToEdit) { mutableStateOf(featureToEdit?.name ?: "") }
    var description by remember(featureToEdit) { mutableStateOf(featureToEdit?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0D1117),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = if (featureToEdit == null) "🩸 Новое умение" else "✏️ Редактировать",
                color = IceBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название (напр. Второе дыхание)") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = IceBlue,
                        unfocusedBorderColor = IceBlue.copy(alpha = 0.3f),
                        focusedLabelColor = IceBlue,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание / Эффект") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = IceBlue,
                        unfocusedBorderColor = IceBlue.copy(alpha = 0.3f),
                        focusedLabelColor = IceBlue,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth().height(120.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val feature = featureToEdit?.copy(name = name, description = description)
                        ?: Feature(name = name, description = description)
                    onSave(feature)
                    onDismiss()
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = IceBlue, contentColor = Color.Black)
            ) {
                Text("Сохранить", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Text("Отмена", color = Color.LightGray)
            }
        }
    )
}


@Preview(showBackground = true, name = "Добавить предмет")
@Composable
fun PreviewAddItemDialog() {
    DnD_sheetTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A24))
        ) {
            ItemDialog(
                onDismiss = {},
                onSaveItem = {}
            )
        }
    }
}