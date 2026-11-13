package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.Feature
import com.example.dnd_sheet.R
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

val IceBlue = Color(0xFF7AD7F0)
val DarkGlassMoon = Color(0x990A1117)
val MoonWhite = Color(0xFFF0F8FF)

@Composable
fun FeaturesScreen(
    character: CharacterAttributes,
    onUpdateProficiencies: (String) -> Unit,
    onAddFeature: (Feature) -> Unit,
    onUpdateFeature: (Feature) -> Unit,
    onRemoveFeature: (Long) -> Unit,
    toBack: () -> Unit,
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var featureToEdit by remember { mutableStateOf<Feature?>(null) }
    var featureToDelete by remember { mutableStateOf<Feature?>(null) }

    var proficienciesText by remember(character.proficiencies) { mutableStateOf(character.proficiencies) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap()
    ) {
        Image(
            painter = painterResource(R.drawable.back_features_screen),
            contentDescription = "Темный охотник",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.85f),
                            Color(0xFF0A1117).copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 45.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = toBack,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, IceBlue.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = IceBlue,
                        containerColor = Color.Black.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Назад", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "\uD83C\uDF00 Умения",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MoonWhite,
                    style = TextStyle(shadow = androidx.compose.ui.graphics.Shadow(color = IceBlue, blurRadius = 12f))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Владения
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGlassMoon),
                border = BorderStroke(1.dp, IceBlue.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "🛡️ ВЛАДЕНИЯ И ЯЗЫКИ",
                        color = IceBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp)
                            .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        if (proficienciesText.isEmpty()) {
                            Text(
                                text = "",
                                color = Color.Gray.copy(alpha = 0.5f),
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                        BasicTextField(
                            value = proficienciesText,
                            onValueChange = {
                                proficienciesText = it
                                onUpdateProficiencies(it)
                            },
                            textStyle = TextStyle(color = MoonWhite, fontSize = 13.sp, lineHeight = 18.sp),
                            cursorBrush = SolidColor(IceBlue),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚔️ УМЕНИЯ И ОСОБЕННОСТИ",
                    color = IceBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = IceBlue.copy(alpha = 0.2f), contentColor = IceBlue),
                    border = BorderStroke(1.dp, IceBlue),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("➕ Добавить", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(character.features, key = { it.id }) { feature ->
                    FeatureCard(
                        feature = feature,
                        onEdit = { featureToEdit = feature },
                        onDelete = { featureToDelete = feature }
                    )
                }
                if (character.features.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Умений пока нет...", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        FeatureDialog(
            featureToEdit = null,
            onDismiss = { showAddDialog = false },
            onSave = { onAddFeature(it) }
        )
    }

    if (featureToEdit != null) {
        FeatureDialog(
            featureToEdit = featureToEdit,
            onDismiss = { featureToEdit = null },
            onSave = { onUpdateFeature(it); featureToEdit = null }
        )
    }

    if (featureToDelete != null) {
        AlertDialog(
            onDismissRequest = { featureToDelete = null },
            containerColor = Color(0xFF0D1117),
            title = { Text("Удалить умение?", color = MoonWhite) },
            text = { Text("Вы уверены, что хотите удалить «${featureToDelete?.name}»?", color = Color.LightGray) },
            confirmButton = {
                Button(
                    onClick = { onRemoveFeature(featureToDelete!!.id); featureToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
                ) { Text("Удалить", color = Color.White) }
            },
            dismissButton = {
                OutlinedButton(onClick = { featureToDelete = null }) { Text("Отмена", color = Color.LightGray) }
            }
        )
    }
}

@Composable
fun FeatureCard(
    feature: Feature,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkGlassMoon),
        border = BorderStroke(1.dp, IceBlue.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = feature.name,
                    color = MoonWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (feature.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = feature.description,
                        color = Color.LightGray.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Text("✏️", fontSize = 14.sp)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Text("🗑️", fontSize = 14.sp)
            }
        }
    }
}


@Preview(showBackground = true, name = "Умения")
@Composable
fun PreviewFeaturesScreen() {
    DnD_sheetTheme {
        FeaturesScreen(
            character = CharacterAttributes(
                nickname = "Арагорн",
                characterClass = "Следопыт",
                proficiencies = "Доспехи: Легкие, Средние\nОружие: Простое, Воинское\nИнструменты: Воровские\nЯзыки: Общий, Эльфийский",
                features = listOf(
                    Feature(
                        name = "Второе дыхание",
                        description = "Бонусным действием вы можете восстановить 1к10 + уровень хитов. Требуется короткий или длительный отдых."
                    ),
                    Feature(
                        name = "Скрытая атака",
                        description = "Один раз в ход вы можете нанести дополнительный урон 1к6, если у вас есть преимущество на атаку."
                    ),
                    Feature(
                        name = "Тёмное зрение",
                        description = "Вы видите в темноте на расстоянии 60 футов как при тусклом свете."
                    )
                )
            ),
            onUpdateProficiencies = {},
            onAddFeature = {},
            onUpdateFeature = {},
            onRemoveFeature = {},
            toBack = {}
        )
    }
}