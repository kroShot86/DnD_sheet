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
import com.example.dnd_sheet.deleteImageFile

val DarkGlass = Color(0xCC101820)
val NeonBlue = Color(0xFF00E5FF)
val DangerRed = Color(0xFFFF5252)
val HealGreen = Color(0xFF69F0AE)

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
    toBack: () -> Unit,
    modifier: Modifier = Modifier
) {


    val context = LocalContext.current

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(130.dp)
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
                        color = Color.White,
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

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGlass),
                border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                        Text("${character.level}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = GoldGlow)
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
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Здоровье", fontSize = 14.sp, color = Color.Gray)
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

            Button(
                onClick = { takeDamage(10) },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.8f)),
                enabled = character.currentState != CharState.DEAD,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (character.currentState == CharState.UNCONSCIOUS) "💀 Добить!" else "⚔️ Получить урон (-10 HP)", fontSize = 16.sp, color = Color.White)
            }

            Button(
                onClick = { heal(5) },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HealGreen.copy(alpha = 0.8f)),
                enabled = (character.currentState != CharState.DEAD) && (character.currentHp < character.maxHp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("💚 Лечение (+5 HP)", fontSize = 16.sp, color = Color.Black)
            }

            Button(
                onClick = { toInventory(character.id) },
                modifier = Modifier.fillMaxWidth().height(50.dp).padding(bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue.copy(alpha = 0.2f)),
                border = BorderStroke(1.dp, NeonBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🎒 Открыть Инвентарь", fontSize = 16.sp, color = NeonBlue)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { reset() },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("🔄 Сброс", color = Color.LightGray)
                }
                OutlinedButton(
                    onClick = { toBack() },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("↩️ Назад", color = Color.LightGray)
                }
            }
        }
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

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C252E)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = NeonBlue, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = textValue,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                        textValue = newValue
                        newValue.toIntOrNull()?.let { onValueChange(it) }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.White),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
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
            toBack = {},
            modifier = Modifier
        )
    }
}