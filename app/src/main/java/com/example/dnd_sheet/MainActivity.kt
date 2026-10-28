package com.example.dnd_sheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DnD_sheetTheme {
                val stateHolder = rememberSaveable(saver = CharacterStateHolderSaver) { CharacterStateHolder() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CharacterScreen(character = stateHolder.character,
                        takeDamage = {value -> stateHolder.takeDamage(value)},
                        heal = {value -> stateHolder.heal(value)},
                        updateStat = {stat, value -> stateHolder.updateStat(stat, value)},
                        reset = {stateHolder.reset()},
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun CharacterScreen(
    character: CharacterAttributes,
    takeDamage: (Int) -> Unit,
    heal: (Int) -> Unit,
    updateStat: (String, Int) -> Unit,
    reset: () -> Unit,
    modifier: Modifier = Modifier
) {

    val backgroundColor = when (character.currentState) {
        CharState.HEALTHY -> Color(0xFFE8F5E9)
        CharState.UNCONSCIOUS -> Color(0xFFFFF3E0)
        CharState.DEAD -> Color(0xFFEF5350)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                .border(2.dp, Color.Gray, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            val avatarEmoji = when (character.currentState) {
                CharState.HEALTHY -> "🧙‍♂️"
                CharState.UNCONSCIOUS -> "😵"
                CharState.DEAD -> "💀"
            }
            Text(text = avatarEmoji, fontSize = 64.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (character.currentState != CharState.HEALTHY) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text(
                    text = when (character.currentState) {
                        CharState.UNCONSCIOUS -> "💀 ВЫ БЕЗ СОЗНАНИЯ!"
                        CharState.DEAD -> "☠️ ВЫ МЕРТВЫ."
                        else -> ""
                    },
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Text("Характеристики", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {

            StatInputField(
                title = "СИЛ",
                value = character.strength.toString(),
                onValueChange = { updateStat("СИЛ", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )

            StatInputField(
                title = "ЛВК",
                value = character.dexterity.toString(),
                onValueChange = { updateStat("ЛВК", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )

            StatInputField(
                title = "ТЕЛ",
                value = character.constitution.toString(),
                onValueChange = { updateStat("ТЕЛ", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()){

            StatInputField(
                title = "ИНТ",
                value = character.intelligence.toString(),
                onValueChange = { updateStat("ИНТ", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )

            StatInputField(
                title = "МДР",
                value = character.wisdom.toString(),
                onValueChange = { updateStat("МДР", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )

            StatInputField(
                title = "ХАР",
                value = character.charisma.toString(),
                onValueChange = { updateStat("ХАР", it.toIntOrNull() ?: 0) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Хиты: ${character.currentHp} / ${character.maxHp}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Text("Действия:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { takeDamage(10) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
            enabled = character.currentState != CharState.DEAD
        ){
            if ( (character.currentState != CharState.UNCONSCIOUS) && (character.currentState != CharState.DEAD) ){
                Text("Получить урон (-10 HP)")
            }
            else{
                Text("Добить!")
            }

        }

        Button(
            onClick = { heal(5) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            enabled = (character.currentState != CharState.DEAD) && (character.currentHp < character.maxHp)
        ){
            Text("Лечение (+5 HP)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                reset()
            },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Сбросить всё")
        }
    }
}

@Composable
fun StatInputField(title: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0))
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                        onValueChange(newValue)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterScreenPreview() {
    DnD_sheetTheme {
        CharacterScreen(
            character = CharacterAttributes(),
            takeDamage = {},
            heal = {},
            updateStat = { _, _ -> },
            reset = {}
        )
    }
}