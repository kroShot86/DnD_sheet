package com.example.dnd_sheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DnD_sheetTheme {
                val stateHolder = rememberSaveable(saver = CharacterStateHolderSaver) { CharacterStateHolder() }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(stateHolder, Modifier.padding(innerPadding))
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DnD_sheetTheme {
        val stateHolder = rememberSaveable(saver = CharacterStateHolderSaver) { CharacterStateHolder() }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppNavigation(stateHolder, Modifier.padding(innerPadding))
        }
    }
}