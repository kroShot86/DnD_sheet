package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.BookGlass
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.DarkShadow
import com.example.dnd_sheet.GoldInk
import com.example.dnd_sheet.R
import com.example.dnd_sheet.WarmText
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


@Composable
fun HistoryScreen(
    character: CharacterAttributes,
    onUpdateAlignment: (String) -> Unit,
    onUpdateBackstory: (String) -> Unit,
    onUpdateIdeals: (String) -> Unit,
    onUpdateBonds: (String) -> Unit,
    onUpdateFlaws: (String) -> Unit,
    onUpdateNotes: (String) -> Unit,
    toBack: () -> Unit,
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
            painter = painterResource(R.drawable.back_history_screen),
            contentDescription = "Фон истории",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
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
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, GoldInk.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldInk)
                ) {
                    Text("↩️ Назад", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "📖 История и Заметки",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldInk,
                    style = TextStyle(shadow = Shadow(DarkShadow, Offset(2f, 2f), 4f))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    BookCard(
                        title = "Имя героя",
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = character.nickname.ifEmpty { "Безымянный" },
                            color = WarmText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    BookCard(
                        title = "Мировоззрение",
                        modifier = Modifier.weight(1f)
                    ) {
                        BookTextField(
                            value = character.alignment,
                            onValueChange = onUpdateAlignment ,
                            placeholder = "Например: Хаотично-добрый"
                        )
                    }
                }


                BookCard(title = "Предыстория") {
                    BookTextField(
                        value = character.backstory,
                        onValueChange = onUpdateBackstory,
                        placeholder = "Откуда пришел ваш герой? Кем он был до приключений?",
                    )
                }


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BookCard(title = "Идеалы", modifier = Modifier.weight(1f)) {
                        BookTextField(
                            value = character.ideals,
                            onValueChange = onUpdateIdeals,
                            placeholder = "Во что он верит?",
                        )
                    }
                    BookCard(title = "Привязанности", modifier = Modifier.weight(1f)) {
                        BookTextField(
                            value = character.bonds,
                            onValueChange = onUpdateBonds,
                            placeholder = "Кого или что он любит?",
                        )
                    }
                }


                BookCard(title = "Слабости") {
                    BookTextField(
                        value = character.flaws,
                        onValueChange = onUpdateFlaws,
                        placeholder = "Что может его погубить?",
                    )
                }


                BookCard(title = "Заметки игрока") {
                    BookTextField(
                        value = character.notes,
                        onValueChange = onUpdateNotes,
                        placeholder = "Записывайте сюда важные события, имена NPC, зацепки сюжета...",
                    )
                }
            }
        }
    }
}


@Composable
fun BookCard(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(BookGlass, RoundedCornerShape(10.dp))
            .border(1.dp, GoldInk.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title.uppercase(),
                color = GoldInk,

                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                style = TextStyle(shadow = Shadow(DarkShadow, Offset(1f, 1f), 2f)),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}


@Composable
fun BookTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                color = WarmText.copy(alpha = 0.3f),
                fontSize = 14.sp
            )
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = WarmText,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            cursorBrush = SolidColor(GoldInk),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true, name = "История")
@Composable
fun PreviewHistoryScreen() {
    DnD_sheetTheme {
        HistoryScreen(
            character = CharacterAttributes(nickname = "Арагорн"),
            onUpdateAlignment = {},
            onUpdateBackstory = {},
            onUpdateIdeals = {},
            onUpdateBonds = {},
            onUpdateFlaws = {},
            onUpdateNotes = {},
            toBack = {}
        )
    }
}