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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.Items
import com.example.dnd_sheet.Type
import com.example.dnd_sheet.formatWeight
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


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