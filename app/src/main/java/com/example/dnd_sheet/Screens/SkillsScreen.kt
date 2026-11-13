package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.DarkShadow
import com.example.dnd_sheet.GoldAccent
import com.example.dnd_sheet.IceGlass
import com.example.dnd_sheet.ProficiencyLevel
import com.example.dnd_sheet.StatType
import com.example.dnd_sheet.Skill
import com.example.dnd_sheet.R
import com.example.dnd_sheet.TextCyan
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

@Composable
fun SkillsScreen(
    character: CharacterAttributes,
    onToggleSkill: (String) -> Unit,
    onToggleSave: (StatType) -> Unit,
    onUpdateSkillBonus: (String, Int) -> Unit,
    onUpdateSaveBonus: (StatType, Int) -> Unit,
    toBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
    ) {
        Image(
            painter = painterResource(R.drawable.back_skills_screen),
            contentDescription = "Фон навыков",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 45.dp, end = 16.dp, bottom = 50.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = toBack,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.5.dp, TextCyan.copy(alpha = 0.7f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextCyan,
                        containerColor = Color.Black.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text("Назад", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("БМ: ", fontSize = 18.sp, color = TextCyan, fontWeight = FontWeight.Bold)
                    Text("+${character.proficiencyBonus}", fontSize = 28.sp, color = GoldAccent, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "СПАСБРОСКИ",
                color = TextCyan.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.height(155.dp)
            ) {
                items(StatType.entries) { stat ->
                    SaveCard(
                        stat = stat,
                        character = character,
                        isProficient = character.proficientSaves.contains(stat),
                        extraBonus = character.saveExtraBonuses[stat] ?: 0,
                        onToggle = { onToggleSave(stat) },
                        onBonusChange = { onUpdateSaveBonus(stat, it) }
                    )
                }
            }

            Text(
                text = "НАВЫКИ",
                color = TextCyan.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 0.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(character.skills.sortedBy { it.name }) { skill ->
                    SkillCard(
                        skill = skill,
                        character = character,
                        onToggle = { onToggleSkill(skill.name) },
                        onBonusChange = { onUpdateSkillBonus(skill.name, it) }
                    )
                }
            }
        }
    }
}

@Composable
fun SaveCard(
    stat: StatType,
    character: CharacterAttributes,
    isProficient: Boolean,
    extraBonus: Int,
    onToggle: () -> Unit,
    onBonusChange: (Int) -> Unit
) {
    val statMod = when (stat) {
        StatType.STR -> Math.floorDiv(character.strength - 10, 2)
        StatType.DEX -> Math.floorDiv(character.dexterity - 10, 2)
        StatType.CON -> Math.floorDiv(character.constitution - 10, 2)
        StatType.INT -> Math.floorDiv(character.intelligence - 10, 2)
        StatType.WIS -> Math.floorDiv(character.wisdom - 10, 2)
        StatType.CHA -> Math.floorDiv(character.charisma - 10, 2)
    }
    val profBonus = if (isProficient) character.proficiencyBonus else 0
    val totalBonus = statMod + profBonus + extraBonus
    val bonusText = if (totalBonus >= 0) "+$totalBonus" else "$totalBonus"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(IceGlass, RoundedCornerShape(10.dp))
            .border(1.dp, TextCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        // ВЕРХНЯЯ ЧАСТЬ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isProficient,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(checkedColor = GoldAccent, uncheckedColor = Color.Gray),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = getStatNameShort(stat),
                    color = TextCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f))
                )
            }
            Text(
                text = bonusText,
                color = GoldAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f))
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(22.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .border(1.dp, TextCyan.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textFieldValue by remember(extraBonus) {
                mutableStateOf(
                    TextFieldValue(
                        text = if (extraBonus == 0) "" else extraBonus.toString(),
                        selection = TextRange(if (extraBonus == 0) 0 else extraBonus.toString().length)
                    )
                )
            }

            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val newText = newValue.text
                    if (newText.length <= 3 && (newText.isEmpty() || newText == "-" || newText.toIntOrNull() != null)) {
                        textFieldValue = newValue.copy(selection = TextRange(newText.length))
                        val value = if (newText == "-" || newText.isEmpty()) 0 else newText.toIntOrNull() ?: 0
                        onBonusChange(value)
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextCyan,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Доп. бонус:", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun SkillCard(
    skill: Skill,
    character: CharacterAttributes,
    onToggle: () -> Unit,
    onBonusChange: (Int) -> Unit
) {
    val statMod = when (skill.stat) {
        StatType.STR -> Math.floorDiv(character.strength - 10, 2)
        StatType.DEX -> Math.floorDiv(character.dexterity - 10, 2)
        StatType.CON -> Math.floorDiv(character.constitution - 10, 2)
        StatType.INT -> Math.floorDiv(character.intelligence - 10, 2)
        StatType.WIS -> Math.floorDiv(character.wisdom - 10, 2)
        StatType.CHA -> Math.floorDiv(character.charisma - 10, 2)
    }
    val profBonus = when (skill.proficiency) {
        ProficiencyLevel.NONE -> 0
        ProficiencyLevel.PROFICIENT -> character.proficiencyBonus
        ProficiencyLevel.EXPERTISE -> character.proficiencyBonus * 2
    }
    val totalBonus = statMod + profBonus + skill.extraBonus
    val bonusText = if (totalBonus >= 0) "+$totalBonus" else "$totalBonus"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .background(IceGlass, RoundedCornerShape(10.dp))
            .border(1.dp, TextCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(6.dp)
    ) {
        Text(
            text = skill.name,
            color = if (skill.proficiency != ProficiencyLevel.NONE) GoldAccent else TextCyan,
            fontWeight = if (skill.proficiency != ProficiencyLevel.NONE) FontWeight.Bold else FontWeight.Medium,
            fontSize = 15.sp,
            maxLines = 1,
            style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f)),
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(0.7f)
        )

        Text(
            text = bonusText,
            color = GoldAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f)),
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(23.dp)
                .border(2.dp, if (skill.proficiency != ProficiencyLevel.NONE) GoldAccent else TextCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .background(
                    when (skill.proficiency) {
                        ProficiencyLevel.NONE -> Color.Transparent
                        ProficiencyLevel.PROFICIENT -> GoldAccent.copy(alpha = 0.6f)
                        ProficiencyLevel.EXPERTISE -> GoldAccent
                    },
                    RoundedCornerShape(4.dp)
                )
                .clickable { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            if (skill.proficiency == ProficiencyLevel.EXPERTISE) {
                Text("★", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 14.sp)
            } else if (skill.proficiency == ProficiencyLevel.PROFICIENT) {
                Text("✓", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 14.sp)
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(110.dp)
                .height(24.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                .border(1.dp, TextCyan.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textFieldValue by remember(skill.extraBonus) {
                mutableStateOf(
                    TextFieldValue(
                        text = if (skill.extraBonus == 0) "" else skill.extraBonus.toString(),
                        selection = TextRange(if (skill.extraBonus == 0) 0 else skill.extraBonus.toString().length)
                    )
                )
            }

            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val newText = newValue.text
                    if (newText.length <= 3 && (newText.isEmpty() || newText == "-" || newText.toIntOrNull() != null)) {
                        textFieldValue = newValue.copy(selection = TextRange(newText.length))
                        val value = if (newText == "-" || newText.isEmpty()) 0 else newText.toIntOrNull() ?: 0
                        onBonusChange(value)
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextCyan,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Доп. бонус:", color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        innerTextField()
                    }
                }
            )
        }
    }
}

fun getStatNameShort(stat: StatType): String {
    return when (stat) {
        StatType.STR -> "СИЛ"
        StatType.DEX -> "ЛОВ"
        StatType.CON -> "ТЕЛ"
        StatType.INT -> "ИНТ"
        StatType.WIS -> "МДР"
        StatType.CHA -> "ХАР"
    }
}

@Preview(showBackground = true, name = "Навыки")
@Composable
fun PreviewSkillsScreen() {
    DnD_sheetTheme {
        SkillsScreen(
            character = CharacterAttributes(nickname = "Арагорн"),
            onToggleSkill = {},
            onToggleSave = {},
            onUpdateSkillBonus = { _, _ -> },
            onUpdateSaveBonus = { _, _ -> },
            toBack = {}
        )
    }
}