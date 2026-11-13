package com.example.dnd_sheet.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.CharState
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.R
import com.example.dnd_sheet.saveImage
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import java.io.File
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.example.dnd_sheet.DangerRed
import com.example.dnd_sheet.DarkGlass
import com.example.dnd_sheet.GoldGlow
import com.example.dnd_sheet.HealGreen
import com.example.dnd_sheet.NeonBlue
import com.example.dnd_sheet.deleteImageFile


@Composable
fun CharacterScreen(
    character: CharacterAttributes,
    takeDamage: (Int) -> Unit,
    heal: (Int) -> Unit,
    updateStat: (String, Int) -> Unit,
    updateExp: (Int) -> Unit,
    updateClass: (String) -> Unit,
    updateNickname: (String) -> Unit,
    updateAvatar: (String?) -> Unit,
    reset: () -> Unit,
    toInventory: (Long) -> Unit,
    toSkills: (Long) -> Unit,
    toHistory: (Long) -> Unit,
    toBack: () -> Unit,
    modifier: Modifier = Modifier
) {


    val context = LocalContext.current

    var showDamageDialog by remember { mutableStateOf(false) }
    var showHealDialog by remember { mutableStateOf(false) }

    val cropLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { croppedUri ->
                deleteImageFile(character.avatarUri)

                val savedPath = saveImage(context, croppedUri)
                if (savedPath != null) {
                    updateAvatar(savedPath)
                }
            }
        } else {
            result.error?.printStackTrace()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            cropLauncher.launch(
                CropImageContractOptions(
                    uri = it,
                    cropImageOptions = CropImageOptions(
                        guidelines = CropImageView.Guidelines.ON,
                        aspectRatioX = 1,
                        aspectRatioY = 1,
                        fixAspectRatio = true,
                        cropShape = CropImageView.CropShape.OVAL,
                        activityTitle = "Обрезка аватара",
                        cropMenuCropButtonTitle = "Готово"
                    )
                )
            )
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.back_character_screen),
            contentDescription = "Лист персонажа",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(DarkGlass)
                        .border(3.dp, NeonBlue, CircleShape)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (character.avatarUri != null) {
                        AsyncImage(
                            model = File(character.avatarUri),
                            contentDescription = "Аватар",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        val avatarEmoji = when (character.currentState) {
                            CharState.HEALTHY -> "🧙‍♂️"
                            CharState.UNCONSCIOUS -> "😵"
                            CharState.DEAD -> "💀"
                        }
                        Text(text = avatarEmoji, fontSize = 72.sp)
                    }
                }

                SectionButton(
                    icon = "🎯",
                    text = "Навыки",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 16.dp)
                ) { toSkills(character.id) }

                SectionButton(
                    icon = "✨",
                    text = "Магия",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 16.dp)
                ) { /* TODO: toMagic */ }


                SectionButton(
                    icon = "📜",
                    text = "История",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 5.dp)
                ) { toHistory(character.id) }


                SectionButton(
                    icon = "🎒",
                    text = "Сумка",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 5.dp)
                ) { toInventory(character.id) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {  },
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = character.nickname,
                    onValueChange = { newNickname ->
                        updateNickname(newNickname)
                    },
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldGlow,
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (character.nickname.isEmpty()) {
                            Text(
                                text = "Инкогнито",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        innerTextField()
                    }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))



            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGlass),
                border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 5.dp, end = 16.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Класс", fontSize = 12.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = character.characterClass,
                            onValueChange = {
                                updateClass(it)
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = NeonBlue,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = NeonBlue,

                            )
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text("Уровень", fontSize = 12.sp, color = Color.Gray)
                        Text("${character.level}", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = GoldGlow)
                        Text("БМ: +${character.proficiencyBonus}", fontSize = 12.sp, color = Color(0xFFC4A96E), fontWeight = FontWeight.Bold)
                    }


                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                        Text("Опыт", fontSize = 12.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = when {
                                character.exp > 0 -> character.exp.toString()
                                else -> ""
                            },
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } && newValue.length <= 9) {
                                    updateExp(newValue.toIntOrNull() ?: 0)
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = NeonBlue, textAlign = TextAlign.End),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = NeonBlue
                            )
                        )
                    }
                }
            }

            if (character.currentState != CharState.HEALTHY) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DangerRed.copy(alpha = 0.8f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = when (character.currentState) {
                            CharState.UNCONSCIOUS -> "💀 ВЫ БЕЗ СОЗНАНИЯ!"
                            CharState.DEAD -> "☠️ ВЫ МЕРТВЫ."
                            else -> ""
                        },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Text(
                "Характеристики",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatInputField("СИЛ", character.strength, { updateStat("СИЛ", it) }, Modifier.weight(1f))
                StatInputField("ЛВК", character.dexterity, { updateStat("ЛВК", it) }, Modifier.weight(1f))
                StatInputField("ТЕЛ", character.constitution, { updateStat("ТЕЛ", it) }, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                StatInputField("ИНТ", character.intelligence, { updateStat("ИНТ", it) }, Modifier.weight(1f))
                StatInputField("МДР", character.wisdom, { updateStat("МДР", it) }, Modifier.weight(1f))
                StatInputField("ХАР", character.charisma, { updateStat("ХАР", it) }, Modifier.weight(1f))
            }


            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGlass),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Здоровье", fontSize = 16.sp, color = Color.Gray)
                    Text("${character.currentHp} / ${character.maxHp}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    Spacer(modifier = Modifier.height(8.dp))

                    val hpFraction = character.currentHp.toFloat() / character.maxHp.toFloat()
                    LinearProgressIndicator(
                        progress = { hpFraction },
                        modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                        color = if (hpFraction > 0.3f) NeonBlue else DangerRed,
                        trackColor = Color.DarkGray
                    )
                }
            }

            Text("Действия", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NeonBlue, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))

            Row (horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { showDamageDialog = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.8f)),
                    enabled = character.currentState != CharState.DEAD,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (character.currentState == CharState.UNCONSCIOUS) "Добить!" else "⚔️ Получить урон ⚔️",
                        fontSize = 14.sp,
                        color = Color.White,

                    )
                }

                Button(
                    onClick = { showHealDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HealGreen.copy(alpha = 0.8f)),
                    //enabled = (character.currentState != CharState.DEAD) && (character.currentHp < character.maxHp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("\uD83D\uDC9B Лечение \uD83D\uDC9B", fontSize = 14.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                OutlinedButton(
                    onClick = { reset() },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GoldGlow
                    )
                ) {
                    Text(
                        text = "Длительный отдых",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { toBack() },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.3f)),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = NeonBlue
                    )
                ) {
                    Text(
                        text = "Список персонажей",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showDamageDialog) {
        NumberInputDialog(
            title = "⚔️ Нанести урон",
            themeColor = DangerRed,
            confirmButtonColor = DangerRed,
            confirmButtonTextColor = Color.White,
            onDismiss = { showDamageDialog = false },
            onConfirm = { amount -> takeDamage(amount) }
        )
    }

    if (showHealDialog) {
        NumberInputDialog(
            title = "💚 Лечение",
            themeColor = HealGreen,
            confirmButtonColor = HealGreen,
            confirmButtonTextColor = Color.Black,
            onDismiss = { showHealDialog = false },
            onConfirm = { amount -> heal(amount) }
        )
    }
}


@Composable
fun StatInputField(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(value) { mutableStateOf(value.toString()) }

    val modifierValue = Math.floorDiv(value - 10, 2)
    val modifierText = if (modifierValue >= 0) "+$modifierValue" else "$modifierValue"

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C252E)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = NeonBlue,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = modifierText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = GoldGlow,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                            textValue = newValue
                            newValue.toIntOrNull()?.let { onValueChange(it) }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}


@Composable
fun SectionButton(
    icon: String,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.size(72.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = DarkGlass),
        border = BorderStroke(2.dp, GoldGlow.copy(alpha = 0.4f)),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(icon, fontSize = 24.sp)
            Text(
                text = text,
                fontSize = 9.sp,
                color = GoldGlow,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 10.sp
            )
        }
    }
}


@Preview(showBackground = true, name = "Лист персонажа")
@Composable
fun PreviewCharacterScreen() {
    DnD_sheetTheme {
        CharacterScreen(
            character = CharacterAttributes(nickname = "Арагорн", characterClass = "Следопыт", currentHp = 25, exp = 5000, level = 4),
            takeDamage = {},
            heal = {},
            updateStat = { _, _ -> },
            updateExp = {},
            updateClass = {},
            updateNickname = { _ -> },
            updateAvatar = { },
            reset = {},
            toInventory = {},
            toSkills = {},
            toHistory = {},
            toBack = {},
            modifier = Modifier
        )
    }
}