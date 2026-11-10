package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAddItem: (Items) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newWeight by remember { mutableStateOf("") }
    var newCount by remember { mutableStateOf("1") }
    var newType by remember { mutableStateOf(Type.ARMORS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xE6151010),
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Новый предмет",
                color = Color(0xFFFFD700),
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
                        Type.ARMORS -> "Оружие"
                        Type.WEAPONS -> "Броня"
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
                            Type.ARMORS,
                            Type.WEAPONS,
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
                        onAddItem(
                            Items(
                                name = newName,
                                weight = weight,
                                count = count,
                                itemType = newType
                            )
                        )
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700), contentColor = Color.Black)
            ) {
                Text("Добавить", fontWeight = FontWeight.Bold)
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


@Preview(showBackground = true, name = "Добавить предмет")
@Composable
fun PreviewAddItemDialog() {
    DnD_sheetTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A24))
        ) {
            AddItemDialog(
                onDismiss = {},
                onAddItem = {}
            )
        }
    }
}