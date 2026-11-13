package com.example.dnd_sheet.Screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dnd_sheet.*
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.Models.Spell
import com.example.dnd_sheet.Models.SpellLevel
import com.example.dnd_sheet.Models.SpellSlots
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import com.example.dnd_sheet.R

@Composable
fun MagikScreen(
    character: CharacterAttributes,
    onAddSpell: (Spell) -> Unit,
    onUpdateSpell: (Spell) -> Unit,
    onRemoveSpell: (Long) -> Unit,
    onTogglePrepared: (Long) -> Unit,
    onUpdateSlot: (SpellLevel, Int) -> Unit,
    onUseSlot: (SpellLevel) -> Unit,
    onRestoreSlot: (SpellLevel) -> Unit,
    onLongRest: () -> Unit,
    toBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var spellToEdit by remember { mutableStateOf<Spell?>(null) }
    var spellToDelete by remember { mutableStateOf<Spell?>(null) }
    var slotToEdit by remember { mutableStateOf<SpellLevel?>(null) }

    val spellsByLevel = character.spells.groupBy { it.level }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clearFocusOnTap()
    ) {
        Image(
            painter = painterResource(R.drawable.back_magik_screen),
            contentDescription = "Магический лес",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A192F).copy(alpha = 0.7f),
                            Color.Black.copy(alpha = 0.85f)
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

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "✨ Магия",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextCyan,
                        style = TextStyle(shadow = Shadow(Color(0xFF00E5FF), Offset(0f, 0f), 12f))
                    )
                    Text(
                        text = "Уровень заклинателя: ${character.level}",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔮 Ячейки", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextCyan)
                TextButton(onClick = onLongRest) {
                    Text("Долгий отдых", color = GoldAccent, fontSize = 12.sp)
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().height(90.dp)
            ) {
                items(SpellLevel.entries.filter { it != SpellLevel.CANTRIP }) { level ->
                    val slots = character.spellSlots[level] ?: SpellSlots(max = 0, used = 0)
                    SpellSlotCard(
                        level = level,
                        slots = slots,
                        onEdit = { slotToEdit = level },
                        onUse = { onUseSlot(level) },
                        onRestore = { onRestoreSlot(level) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📖 Заклинания", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextCyan)
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = TextCyan.copy(alpha = 0.2f), contentColor = TextCyan),
                    border = BorderStroke(1.dp, TextCyan),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("➕ Добавить", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                SpellLevel.entries.forEach { level ->
                    val spells = spellsByLevel[level].orEmpty()
                    if (spells.isNotEmpty() || level == SpellLevel.CANTRIP) {
                        item(key = level.name) {
                            Text(
                                text = level.displayName,
                                color = GoldAccent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(spells, key = { it.id }) { spell ->
                            SpellCard(
                                spell = spell,
                                onTogglePrepared = { onTogglePrepared(spell.id) },
                                onEdit = { spellToEdit = spell },
                                onDelete = { spellToDelete = spell }
                            )
                        }
                    }
                }
                if (character.spells.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Заклинаний пока нет...", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }


    if (showAddDialog) {
        SpellDialog(
            spellToEdit = null,
            onDismiss = { showAddDialog = false },
            onSave = { onAddSpell(it) }
        )
    }

    if (spellToEdit != null) {
        SpellDialog(
            spellToEdit = spellToEdit,
            onDismiss = { spellToEdit = null },
            onSave = { onUpdateSpell(it); spellToEdit = null }
        )
    }

    if (spellToDelete != null) {
        AlertDialog(
            onDismissRequest = { spellToDelete = null },
            containerColor = Color(0xE6101520),
            title = { Text("Удалить заклинание?", color = Color.White) },
            text = { Text("Вы уверены, что хотите удалить «${spellToDelete?.name}»?", color = Color.LightGray) },
            confirmButton = {
                Button(
                    onClick = { onRemoveSpell(spellToDelete!!.id); spellToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) { Text("Удалить") }
            },
            dismissButton = {
                OutlinedButton(onClick = { spellToDelete = null }) { Text("Отмена", color = Color.LightGray) }
            }
        )
    }

    if (slotToEdit != null) {
        val currentMax = character.spellSlots[slotToEdit!!]?.max ?: 0
        SpellSlotDialog(
            currentMax = currentMax,
            onDismiss = { slotToEdit = null },
            onSave = { onUpdateSlot(slotToEdit!!, it); slotToEdit = null }
        )
    }
}

@Composable
fun SpellSlotCard(
    level: SpellLevel,
    slots: SpellSlots,
    onEdit: () -> Unit,
    onUse: () -> Unit,
    onRestore: () -> Unit
) {

    val totalDisplayed = slots.max.coerceAtMost(5)
    val available = (slots.max - slots.used).coerceAtLeast(0)

    Box(
        modifier = Modifier
            .width(70.dp)
            .fillMaxHeight()
            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .border(1.dp, TextCyan.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .clickable { onEdit() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = level.displayName.replace(" уровень", " ур."),
                color = TextCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Кружки ячеек
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(totalDisplayed) { index ->
                    val isAvailable = index < available

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isAvailable) TextCyan else Color.Transparent)
                            .border(1.dp, TextCyan, CircleShape)
                            .clickable {
                                if (isAvailable) onUse() else onRestore()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${slots.max - slots.used}/${slots.max}",
                color = GoldAccent,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SpellCard(
    spell: Spell,
    onTogglePrepared: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC0A192F)),
        border = BorderStroke(1.dp, if (spell.isPrepared) TextCyan else Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(2.dp, if (spell.isPrepared) TextCyan else Color.Gray, CircleShape)
                    .background(if (spell.isPrepared) TextCyan else Color.Transparent, CircleShape)
                    .clickable { onTogglePrepared() }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = spell.name,
                    color = if (spell.isPrepared) TextCyan else Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                if (spell.description.isNotEmpty()) {
                    Text(
                        text = spell.description,
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                Text("✏️", fontSize = 16.sp)
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Text("🗑️", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Магия")
@Composable
fun PreviewMagikScreen() {
    DnD_sheetTheme {
        MagikScreen(
            character = CharacterAttributes(nickname = "Арагорн"),
            onAddSpell = {},
            onUpdateSpell = {},
            onRemoveSpell = {},
            onTogglePrepared = {},
            onUpdateSlot = { _, _ -> },
            onUseSlot = {},
            onRestoreSlot = {},
            onLongRest = {},
            toBack = {}
        )
    }
}