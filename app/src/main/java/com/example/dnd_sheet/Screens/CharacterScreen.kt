package com.example.dnd_sheet.Screens

import CharactersListIcon
import HistoryBookIcon
import InventoryPotionIcon
import MagicPortalIcon
import SkillsTargetIcon
import FeaturesCrystalIcon
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.Models.CharState
import com.example.dnd_sheet.Models.CharacterAttributes
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
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.deleteImageFile
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange


@Composable
fun CharacterScreen(
    character: CharacterAttributes,
    takeDamage: (Int) -> Unit,
    heal: (Int) -> Unit,
    updateDeathSave: (Boolean, Int) -> Unit,
    updateStat: (String, Int) -> Unit,
    updateExp: (Int) -> Unit,
    updateClass: (String) -> Unit,
    updateNickname: (String) -> Unit,
    updateAvatar: (String?) -> Unit,
    reset: () -> Unit,
    updateMaxHp: (Int) -> Unit,
    updateHitDice: (Int) -> Unit,
    toInventory: (Long) -> Unit,
    toSkills: (Long) -> Unit,
    toHistory: (Long) -> Unit,
    toMagik: (Long) -> Unit,
    toFeatures: (Long) -> Unit,
    toBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    var expTextFieldValue by remember(character.exp) {
        mutableStateOf(
            TextFieldValue(
                text = if (character.exp > 0) character.exp.toString() else "",
                selection = TextRange(if (character.exp > 0) character.exp.toString().length else 0)
            )
        )
    }

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



    Box(modifier = Modifier.fillMaxSize().clearFocusOnTap()) {
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
                .padding(start = 16.dp, top = 15.dp, end = 16.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 3.dp.toPx()
                        drawCircle(
                            brush = Brush.sweepGradient(
                                colors = listOf(
                                    NeonBlue,
                                    Color.White,
                                    Color(0xFF9D00FF),
                                    GoldGlow,
                                    NeonBlue
                                )
                            ),
                            radius = size.minDimension / 2 - strokeWidth / 2,
                            style = Stroke(width = strokeWidth)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(194.dp)
                            .clip(CircleShape)
                            .background(DarkGlass),
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
                }


                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionButton(
                        type = SectionType.SKILLS,
                        text = "Навыки"
                    ) { toSkills(character.id) }

                    SectionButton(
                        type = SectionType.MAGIC,
                        text = "Магия"
                    ) { toMagik(character.id) }



                    SectionButton(
                        type = SectionType.FEATURES,
                        text = "Умения"
                    ) { toFeatures(character.id) }
                }


                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    SectionButton(
                        type = SectionType.INVENTORY,
                        text = "Рюкзак"
                    ) { toInventory(character.id) }

                    SectionButton(
                        type = SectionType.HISTORY,
                        text = "История"
                    ) { toHistory(character.id) }

                    SectionButton(
                        type = SectionType.CHARACTERS,
                        text = "Персонажи"
                    ) { toBack() }
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
                        color = GoldGlow,
                        fontSize = 35.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(GoldGlow),
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
                            value = expTextFieldValue,
                            onValueChange = { newValue ->
                                if (newValue.text.all { it.isDigit() } && newValue.text.length <= 9) {
                                    expTextFieldValue = newValue.copy(selection = TextRange(newValue.text.length))

                                    val newExp = newValue.text.toIntOrNull() ?: 0
                                    updateExp(newExp)
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
                    colors = CardDefaults.cardColors(containerColor = DangerRed.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, DangerRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (character.currentState == CharState.DEAD) {
                            Text(
                                text = "☠️ ВЫ МЕРТВЫ",
                                color = DangerRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        } else {
                            Text(
                                text = "💀 СПАСБРОСКИ ОТ СМЕРТИ",
                                color = DangerRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Успехи", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        repeat(3) { index ->
                                            val isChecked = index < character.deathSavesSuccesses
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isChecked) HealGreen else Color.Transparent)
                                                    .border(2.dp, if (isChecked) HealGreen else Color.Gray, CircleShape)
                                                    .clickable {
                                                        updateDeathSave(true, if (isChecked) -1 else 1)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isChecked) {
                                                    Text("✓", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                }
                                            }
                                        }
                                    }
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Провалы", color = Color.LightGray, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        repeat(3) { index ->
                                            val isChecked = index < character.deathSavesFailures
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isChecked) DangerRed else Color.Transparent)
                                                    .border(2.dp, if (isChecked) DangerRed else Color.Gray, CircleShape)
                                                    .clickable {
                                                        updateDeathSave(false, if (isChecked) -1 else 1)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (isChecked) {
                                                    Text("✗", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
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
                Column(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Здоровье", fontSize = 16.sp, color = Color.Gray)

                    var maxHpTextFieldValue by remember(character.maxHp) {
                        mutableStateOf(
                            TextFieldValue(
                                text = character.maxHp.toString(),
                                selection = TextRange(character.maxHp.toString().length)
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "${character.currentHp}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            " / ",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )

                        BasicTextField(
                            value = maxHpTextFieldValue,
                            onValueChange = { newValue ->
                                if (newValue.text.length <= 3) {
                                    // Принудительно ставим курсор в самый конец строки при каждом изменении
                                    maxHpTextFieldValue = newValue.copy(
                                        selection = TextRange(newValue.text.length)
                                    )

                                    val newMax = newValue.text.toIntOrNull()
                                    if (newMax != null && newMax > 0) {
                                        updateMaxHp(newMax)
                                    }
                                }
                            },
                            textStyle = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonBlue,
                                textAlign = TextAlign.Start
                            ),
                            cursorBrush = SolidColor(NeonBlue),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(55.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val hpFraction = character.currentHp.toFloat() / character.maxHp.toFloat()
                    LinearProgressIndicator(
                        progress = { hpFraction },
                        modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(6.dp)),
                        color = if (hpFraction > 0.3f) NeonBlue else DangerRed,
                        trackColor = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎲 Кости хитов: ",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        IconButton(
                            onClick = { updateHitDice(1) },
                            enabled = character.currentCountDice > 0,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text("−", fontSize = 20.sp, color = if (character.currentCountDice > 0) GoldGlow else Color.DarkGray)
                        }

                        Text(
                            text = "${character.currentCountDice} / ${character.maxCountDice}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldGlow,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { updateHitDice(-1) },
                            enabled = character.currentCountDice < character.maxCountDice,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text("+", fontSize = 20.sp, color = if (character.currentCountDice < character.maxCountDice) HealGreen else Color.DarkGray)
                        }
                    }

                }
            }

            Text("Действия", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NeonBlue, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(8.dp))

            Row (horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (character.currentState == CharState.UNCONSCIOUS) {
                            takeDamage(1)
                        } else {
                            showDamageDialog = true
                        }
                    },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed.copy(alpha = 0.8f)),
                    enabled = character.currentState != CharState.DEAD,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (character.currentState == CharState.UNCONSCIOUS) "Умереть" else "⚔️ Получить урон ⚔️",
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
                    enabled = (character.currentState != CharState.DEAD),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("\uD83D\uDC9B Лечение \uD83D\uDC9B", fontSize = 14.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { reset() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.3f)),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = GoldGlow
                )
            ) {
                Text(
                    text = "Продолжительный отдых",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
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
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        if (newValue.text.all { it.isDigit() } && newValue.text.length <= 3) {
                            textFieldValue = newValue.copy(selection = TextRange(newValue.text.length))

                            if (newValue.text.isNotEmpty()) {
                                val newInt = newValue.text.toIntOrNull() ?: 0
                                onValueChange(newInt)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    ),
                    cursorBrush = SolidColor(NeonBlue),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}


enum class SectionType { SKILLS, MAGIC, HISTORY, FEATURES, INVENTORY, CHARACTERS }

@Composable
fun SectionButton(
    type: SectionType,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shadowColor1 = when (type) {
        SectionType.MAGIC -> Color(0xFF9D00FF).copy(alpha = 0.8f)
        SectionType.HISTORY -> Color(0xFFFFD700).copy(alpha = 0.6f)
        SectionType.FEATURES -> Color(0xFF00E5FF).copy(alpha = 0.6f)
        else -> NeonBlue.copy(alpha = 0.6f)
    }
    val shadowColor2 = when (type) {
        SectionType.MAGIC -> Color(0xFF9D00FF).copy(alpha = 0.6f)
        SectionType.HISTORY -> Color(0xFF8B4513).copy(alpha = 0.5f)
        SectionType.FEATURES -> Color(0xFF9D00FF).copy(alpha = 0.5f)
        else -> GoldGlow.copy(alpha = 0.4f)
    }

    val elevation = if (type == SectionType.MAGIC) 16.dp else 12.dp

    Box(
        modifier = modifier
            .size(64.dp)
            .shadow(
                elevation = elevation,
                shape = CircleShape,
                ambientColor = shadowColor1,
                spotColor = shadowColor2
            )
            .background(
                brush = Brush.radialGradient(
                    colors = when (type) {
                        SectionType.MAGIC -> listOf(Color(0xFF0A192F), Color.Black)
                        SectionType.HISTORY -> listOf(Color(0xFF2A1B1B), Color.Black)
                        else -> listOf(DarkGlass.copy(alpha = 0.9f), Color.Black.copy(alpha = 0.95f))
                    }
                ),
                shape = CircleShape
            )
            .border(
                width = 2.dp,
                brush = when (type) {
                    SectionType.MAGIC -> Brush.sweepGradient(listOf(NeonBlue, Color.White, Color(0xFF9D00FF), NeonBlue))
                    SectionType.HISTORY -> Brush.sweepGradient(listOf(GoldGlow, Color.White, Color(0xFF8B4513), GoldGlow))
                    SectionType.FEATURES -> Brush.sweepGradient(listOf(Color(0xFF9D00FF), Color.White, NeonBlue, Color(0xFF9D00FF)))
                    SectionType.CHARACTERS -> Brush.sweepGradient(listOf(GoldGlow, Color.White, Color(0xFF9D00FF), GoldGlow))
                    else -> Brush.sweepGradient(listOf(NeonBlue, Color.White, GoldGlow, NeonBlue))
                },
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            when (type) {
                SectionType.MAGIC -> MagicPortalIcon(modifier = Modifier.size(28.dp))
                SectionType.SKILLS -> SkillsTargetIcon(modifier = Modifier.size(28.dp))
                SectionType.HISTORY -> HistoryBookIcon(modifier = Modifier.size(28.dp))
                SectionType.INVENTORY -> InventoryPotionIcon(modifier = Modifier.size(28.dp))
                SectionType.FEATURES -> FeaturesCrystalIcon(modifier = Modifier.size(28.dp))
                SectionType.CHARACTERS -> CharactersListIcon(modifier = Modifier.size(28.dp))
            }

            Text(
                text = text,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 9.sp,
                style = TextStyle(
                    brush = Brush.verticalGradient(
                        colors = when (type) {
                            SectionType.MAGIC -> listOf(Color.White, NeonBlue)
                            SectionType.HISTORY -> listOf(Color(0xFFF5E6D3), GoldGlow)
                            SectionType.INVENTORY -> listOf(Color.White, Color(0xFF69F0AE))
                            SectionType.FEATURES -> listOf(Color.White, Color(0xFF9D00FF))
                            else -> listOf(GoldGlow, Color(0xFFC4A96E))
                        }
                    )
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
            character = CharacterAttributes(nickname = "Арагорн", characterClass = "Следопыт", currentHp = 10, exp = 5000, level = 4),
            takeDamage = {},
            heal = {},
            updateDeathSave = {_,_ ->},
            updateStat = { _, _ -> },
            updateExp = {},
            updateClass = {},
            updateNickname = { _ -> },
            updateAvatar = { },
            reset = {},
            updateMaxHp = {},
            updateHitDice = {},
            toInventory = {},
            toSkills = {},
            toHistory = {},
            toMagik = {},
            toFeatures = {},
            toBack = {},
            modifier = Modifier
        )
    }
}