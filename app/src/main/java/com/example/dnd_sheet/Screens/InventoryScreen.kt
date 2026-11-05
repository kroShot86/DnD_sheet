package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dnd_sheet.CharacterAttributes
import com.example.dnd_sheet.CharacterViewModel
import com.example.dnd_sheet.Items
import com.example.dnd_sheet.R
import com.example.dnd_sheet.Type
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme

@Composable
fun Inventory(
    character: CharacterAttributes,
    addItem: (item: Items) -> Unit,
    removeItem: (itemId: Long) -> Unit,
    toBack: () -> Unit,

    modifier: Modifier = Modifier
){
    Box(modifier = modifier.fillMaxSize()){

        Image(
            painter = painterResource(R.drawable.inventory),
            contentDescription = "Инвентарь",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Button(onClick = toBack, modifier = Modifier.padding(bottom = 8.dp)) {
                Text("Назад к персонажу")
            }

            Text("Инвентарь: ${character.nickname}", style = MaterialTheme.typography.titleLarge)
            Text("Общий вес: ${character.totalWeight} кг")


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { addItem(Items(name = "Меч", weight = 3f, count = 1, itemType = Type.WEAPON)) }) {
                    Text("+ Оружие")
                }
                Button(onClick = { addItem(Items(name = "Броня", weight = 10f, count = 1, itemType = Type.ARMOR)) }) {
                    Text("+ Броня")
                }
                Button(onClick = { addItem(Items(name = "Зелье", weight = 0.5f, count = 1, itemType = Type.POTION)) }) {
                    Text("+ Зелье")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(character.inventory, key = { it.id }) { item ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("Тип: ${item.itemType}")
                                Text("Вес: ${item.weight} x ${item.count} = ${item.totalWeight} кг")
                            }
                            IconButton(onClick = { removeItem(item.id) }) {
                                Text("❌")
                            }
                        }
                    }
                }
            }
        }

    }

}



@Preview(showBackground = true, name = "Лист персонажа")
@Composable
fun PreviewInventoryScreen() {
    DnD_sheetTheme {
        Inventory(
            character = CharacterAttributes(inventory = listOf(
                Items(id = 1, name = "Меч"),
                Items(id = 2, name = "Щит")
            )),
            addItem = { _ -> },
            removeItem = { _ -> },
            toBack = {  },
            modifier = Modifier

        )
    }
}
