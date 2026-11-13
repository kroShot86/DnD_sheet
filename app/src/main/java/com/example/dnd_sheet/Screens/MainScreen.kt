package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dnd_sheet.Models.CharState
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.DragonFire
import com.example.dnd_sheet.GlassDark
import com.example.dnd_sheet.GoldGlow
import com.example.dnd_sheet.LightningBlue
import com.example.dnd_sheet.R
import com.example.dnd_sheet.clearFocusOnTap
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import java.io.File


@Composable
fun MainScreen(
    characters: List<CharacterAttributes>,
    onCharacterClick: (Long) -> Unit,
    onAddCharacter: () -> Unit,
    onRemoveCharacter: (Long) -> Unit,

) {

    var characterToDelete by remember { mutableStateOf<CharacterAttributes?>(null) }

    Box (modifier = Modifier.clearFocusOnTap()) {
        Image(
            painter = painterResource(R.drawable.back_main_screen),
            contentDescription = "Фон",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "|| Персонажи ||",
                color = GoldGlow,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Button(
                onClick = onAddCharacter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF).copy(alpha = 0.8f),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "+ Создать персонажа",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(characters, key = { it.id }) { character ->
                    CharacterCard(
                        character = character,
                        onClick = { onCharacterClick(character.id) },
                        onRemove = { characterToDelete = character }
                    )
                }
            }
        }
    }

    if (characterToDelete != null) {
        DeleteCharacterDialog(
            characterToDelete = characterToDelete!!,
            onDismiss = { characterToDelete = null },
            onConfirm = { id -> onRemoveCharacter(id) }
        )
    }
}

@Composable
fun CharacterCard(
    character: CharacterAttributes,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    val stateColor = when (character.currentState) {
        CharState.HEALTHY -> LightningBlue
        CharState.UNCONSCIOUS -> GoldGlow
        CharState.DEAD -> DragonFire
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GlassDark),
        border = BorderStroke(
            width = 1.dp,
            color = stateColor.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(stateColor)
            )

            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
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
                    Text(text = avatarEmoji, fontSize = 28.sp)
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    Text(
                        text = character.nickname.ifEmpty { "Инкогнито" },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldGlow,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = character.characterClass.ifEmpty { "(без класса)" },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC4A96E),
                            maxLines = 1
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 16.dp, bottom = 16.dp, end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Уровень: ", fontSize = 14.sp, color = Color(0xFFA68A4D))
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = character.level.toString(),
                            fontSize = 14.sp,
                            color = Color(0xFFFFD54F),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\uD83D\uDC9B ${character.currentHp}/${character.maxHp}",
                        fontSize = 14.sp,
                        color = Color(0xFFFFD54F),
                        maxLines = 1
                    )
                }
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "❌", fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Главный экран")
@Composable
fun PreviewMainScreen() {
    DnD_sheetTheme {
        MainScreen(
            characters = listOf(
                CharacterAttributes(id = 1, nickname = "Арагорн", currentHp = 30),
                CharacterAttributes(id = 2, nickname = "Леголас", currentHp = 12),
                CharacterAttributes(id = 3, nickname = "Гэндальф", currentHp = 0, currentState = CharState.UNCONSCIOUS),
                CharacterAttributes(id = 4, nickname = "Боромир", currentHp = 0, currentState = CharState.DEAD),
            ),
            onAddCharacter = {},
            onCharacterClick = {},
            onRemoveCharacter = {}
        )
    }
}