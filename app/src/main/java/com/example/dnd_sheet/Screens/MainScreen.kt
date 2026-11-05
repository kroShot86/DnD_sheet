package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.CharState
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.R
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


val GlassDark = Color(0xCC1A1A24)
val DragonFire = Color(0xFFFF5252)
val LightningBlue = Color(0xFF00E5FF)
val GoldGlow = Color(0xFFFFD700)

@Composable
fun MainScreen(
    characters: List<CharacterAttributes>,
    onCharacterClick: (Long) -> Unit,
    onAddCharacter: () -> Unit,
    onRemoveCharacter: (Long) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Персонажи",
                color = Color.White,
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
                        onRemove = { onRemoveCharacter(character.id) }
                    )
                }
            }
        }
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
        colors = CardDefaults.cardColors(
            containerColor = GlassDark
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = stateColor.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(stateColor)
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                val emoji = when (character.currentState) {
                    CharState.HEALTHY -> "🧙‍♂️"
                    CharState.UNCONSCIOUS -> "😵"
                    CharState.DEAD -> "💀"
                }
                Text(text = emoji, fontSize = 28.sp)
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = character.nickname.ifEmpty { "Безымянный герой" },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "❤️ ${character.currentHp}/${character.maxHp}",
                        fontSize = 14.sp,
                        color = Color.LightGray
                    )
                }
            }


            IconButton(
                onClick = onRemove,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = "🗑️", fontSize = 20.sp)
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
                CharacterAttributes(id = 1, nickname = "Арагорн", currentHp = 45),
                CharacterAttributes(id = 2, nickname = "Леголас", currentHp = 12),
                CharacterAttributes(id = 3, nickname = "Гэндальф", currentHp = 0, currentState = CharState.UNCONSCIOUS),
                CharacterAttributes(id = 4, nickname = "Боромир", currentHp = 0, currentState = CharState.DEAD),
            ),
            onAddCharacter = {},
            onCharacterClick = {},
            onRemoveCharacter = {},
            modifier = Modifier
        )
    }
}