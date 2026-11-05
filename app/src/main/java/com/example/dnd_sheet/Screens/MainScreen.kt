package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dnd_sheet.CharState
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.R
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

@Composable
fun MainScreen(
    characters: List<CharacterAttributes>,
    onCharacterClick: (Long) -> Unit,
    onAddCharacter: () -> Unit,
    onRemoveCharacter: (Long) -> Unit,
    modifier: Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "Фон",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Button(
                onClick = onAddCharacter,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Создать персонажа")
            }


            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(characters, key = { it.id }) { character ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = when (character.currentState) {
                                CharState.HEALTHY -> Color(0xFFE8F5E9)
                                CharState.UNCONSCIOUS -> Color(0xFFFFF3E0)
                                CharState.DEAD -> Color(0xFFEF5350)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onCharacterClick(character.id) }
                            ) {
                                Text("че то", fontWeight = FontWeight.Bold)
                                Text("HP: ${character.currentHp}/${character.maxHp}")
                                Text(
                                    when (character.currentState) {
                                        CharState.HEALTHY -> "Здоров"
                                        CharState.UNCONSCIOUS -> "Без сознания"
                                        CharState.DEAD -> "Мертв"
                                    }
                                )
                            }

                            IconButton(onClick = { onRemoveCharacter(character.id) }) {
                                Text("❌")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Главный экран")
@Composable
fun PreviewMainScreen(){
    DnD_sheetTheme {
        MainScreen(
            characters = listOf(
                CharacterAttributes(id = 1, nickname = "Персонаж 1", currentHp = 30),
                CharacterAttributes(id = 2, nickname = "Персонаж 2", currentHp = 5),
                CharacterAttributes(id = 3, nickname = "Персонаж 3", currentHp = 0, currentState = CharState.UNCONSCIOUS),
                CharacterAttributes(id = 4, nickname = "Персонаж 4", currentHp = 0, currentState = CharState.DEAD),
            ),
            onAddCharacter = {  },
            onCharacterClick = {  },
            onRemoveCharacter = {  },
            modifier = Modifier
        )
    }
}