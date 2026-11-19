package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
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
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.DarkShadow
import com.example.dnd_sheet.GoldAccent
import com.example.dnd_sheet.IceGlass
import com.example.dnd_sheet.Models.ProficiencyLevel
import com.example.dnd_sheet.Models.StatType
import com.example.dnd_sheet.Models.Skill
import com.example.dnd_sheet.R
import com.example.dnd_sheet.TextCyan
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

@Composable
fun SkillsScreen(
    character: CharacterAttributes,
    onToggleSkill: (String) -> Unit,
    onToggleSave: (StatType) -> Unit,
    onUpdateSkillBonus: (String, Int) -> Unit,
    onUpdateSaveBonus: (StatType, Int) -> Unit,
    onUpdateStat: (String, Int) -> Unit,
    toBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap()
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
                .verticalScroll(rememberScrollState()),
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
                text = "ХАРАКТЕРИСТИКИ",
                color = TextCyan.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatInputField("СИЛ", character.strength, { onUpdateStat("СИЛ", it) }, Modifier.weight(1f))
                StatInputField("ЛВК", character.dexterity, { onUpdateStat("ЛВК", it) }, Modifier.weight(1f))
                StatInputField("ТЕЛ", character.constitution, { onUpdateStat("ТЕЛ", it) }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatInputField("ИНТ", character.intelligence, { onUpdateStat("ИНТ", it) }, Modifier.weight(1f))
                StatInputField("МДР", character.wisdom, { onUpdateStat("МДР", it) }, Modifier.weight(1f))
                StatInputField("ХАР", character.charisma, { onUpdateStat("ХАР", it) }, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = "СПАСБРОСКИ",
                color = TextCyan.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val saves = StatType.entries
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    saves.take(3).forEach { stat ->
                        SaveCard(
                            stat = stat,
                            character = character,
                            isProficient = character.proficientSaves.contains(stat),
                            extraBonus = character.saveExtraBonuses[stat] ?: 0,
                            onToggle = { onToggleSave(stat) },
                            onBonusChange = { onUpdateSaveBonus(stat, it) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    saves.drop(3).forEach { stat ->
                        SaveCard(
                            stat = stat,
                            character = character,
                            isProficient = character.proficientSaves.contains(stat),
                            extraBonus = character.saveExtraBonuses[stat] ?: 0,
                            onToggle = { onToggleSave(stat) },
                            onBonusChange = { onUpdateSaveBonus(stat, it) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Text(
                text = "НАВЫКИ",
                color = TextCyan.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                character.skills.chunked(2).forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pair.forEach { skill ->
                            SkillCard(
                                skill = skill,
                                character = character,
                                onToggle = { onToggleSkill(skill.name) },
                                onBonusChange = { onUpdateSkillBonus(skill.name, it) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
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
    onBonusChange: (Int) -> Unit,
    modifier: Modifier = Modifier
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


    val borderColor = if (isProficient) GoldAccent else TextCyan.copy(alpha = 0.2f)
    val bgColor = if (isProficient) Color(0xFF142830) else Color(0xFF0A1117)

    Box(
        modifier = modifier
            .height(75.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .background(bgColor, RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isProficient,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = GoldAccent,
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.Black
                    ),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = getStatNameShort(stat),
                    color = TextCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f))
                )
            }
            Text(
                text = bonusText,
                color = GoldAccent,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                style = TextStyle(shadow = Shadow(GoldAccent.copy(alpha = 0.5f), Offset(0f, 0f), 8f))
            )
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textFieldValue by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = if (extraBonus == 0) "" else extraBonus.toString(),
                        selection = TextRange(if (extraBonus == 0) 0 else extraBonus.toString().length)
                    )
                )
            }

            LaunchedEffect(extraBonus) {
                val expected = if (extraBonus == 0) "" else extraBonus.toString()
                if (textFieldValue.text != expected) {
                    textFieldValue = TextFieldValue(expected, selection = TextRange(expected.length))
                }
            }

            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val newText = newValue.text
                    if (newText.length <= 3 && (newText.isEmpty() || newText == "-" || newText.toIntOrNull() != null)) {
                        textFieldValue = newValue.copy(selection = TextRange(newText.length))
                        onBonusChange(if (newText == "-" || newText.isEmpty()) 0 else newText.toIntOrNull() ?: 0)
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextCyan,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = SolidColor(TextCyan),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Доп. бонус:", color = Color.Gray, fontSize = 8.sp, fontWeight = FontWeight.Bold)
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
    onBonusChange: (Int) -> Unit,
    modifier: Modifier = Modifier
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

    val isTrained = skill.proficiency != ProficiencyLevel.NONE
    val borderColor = if (isTrained) GoldAccent.copy(alpha = 0.6f) else TextCyan.copy(alpha = 0.2f)
    val bgColor = if (isTrained) Color(0xFF1A2530) else Color(0xFF0F151C)

    Box(
        modifier = modifier
            .height(80.dp)
            .shadow(4.dp, RoundedCornerShape(10.dp))
            .background(bgColor, RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .padding(8.dp)
    ) {
        Text(
            text = skill.name,
            color = if (isTrained) GoldAccent else TextCyan,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth(0.75f)
        )

        Text(
            text = bonusText,
            color = if (isTrained) GoldAccent else Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            style = TextStyle(
                shadow = if (isTrained) Shadow(GoldAccent.copy(alpha = 0.5f), Offset(0f, 0f), 8f) else null
            ),
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(24.dp)
                .border(
                    width = 2.dp,
                    color = if (isTrained) GoldAccent else TextCyan.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(4.dp)
                )
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
            when (skill.proficiency) {
                ProficiencyLevel.EXPERTISE -> Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                ProficiencyLevel.PROFICIENT -> Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                ProficiencyLevel.NONE -> Unit
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(100.dp)
                .height(22.dp)
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textFieldValue by remember {
                mutableStateOf(
                    TextFieldValue(
                        text = if (skill.extraBonus == 0) "" else skill.extraBonus.toString(),
                        selection = TextRange(if (skill.extraBonus == 0) 0 else skill.extraBonus.toString().length)
                    )
                )
            }

            LaunchedEffect(skill.extraBonus) {
                val expected = if (skill.extraBonus == 0) "" else skill.extraBonus.toString()
                if (textFieldValue.text != expected) {
                    textFieldValue = TextFieldValue(expected, selection = TextRange(expected.length))
                }
            }

            BasicTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val newText = newValue.text
                    if (newText.length <= 3 && (newText.isEmpty() || newText == "-" || newText.toIntOrNull() != null)) {
                        textFieldValue = newValue.copy(selection = TextRange(newText.length))
                        onBonusChange(if (newText == "-" || newText.isEmpty()) 0 else newText.toIntOrNull() ?: 0)
                    }
                },
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = TextCyan,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = SolidColor(TextCyan),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("Бонус:", color = Color.Gray, fontSize = 8.sp)
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

@Composable
fun StatInputField(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textFieldValue by remember(value) {
        mutableStateOf(
            TextFieldValue(
                text = value.toString(),
                selection = TextRange(value.toString().length)
            )
        )
    }

    val modifierValue = Math.floorDiv(value - 10, 2)
    val modifierText = if (modifierValue >= 0) "+$modifierValue" else "$modifierValue"

    Card(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(12.dp), ambientColor = Color.Black, spotColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, TextCyan.copy(alpha = 0.4f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E2A38),
                            Color(0xFF0F151C)
                        )
                    )
                )
                .padding(vertical = 8.dp, horizontal = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Text(
                    text = modifierText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GoldAccent,
                    modifier = Modifier.padding(vertical = 2.dp),
                    style = TextStyle(
                        shadow = Shadow(color = GoldAccent.copy(alpha = 0.5f), blurRadius = 10f)
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .border(1.dp, TextCyan.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    BasicTextField(
                        value = textFieldValue,
                        onValueChange = { newValue ->
                            if (newValue.text.all { it.isDigit() } && newValue.text.length <= 3) {
                                textFieldValue = newValue.copy(selection = TextRange(newValue.text.length))
                                if (newValue.text.isNotEmpty()) {
                                    onValueChange(newValue.text.toIntOrNull() ?: 0)
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 18.sp
                        ),
                        cursorBrush = SolidColor(TextCyan),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
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
            onUpdateStat = {_, _ ->},
            toBack = {}
        )
    }
}