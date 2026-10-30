package com.example.dnd_sheet.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.dnd_sheet.AppNavigation
import com.example.dnd_sheet.CharacterStateHolder
import com.example.dnd_sheet.CharacterStateHolderSaver
import com.example.dnd_sheet.R
import com.example.dnd_sheet.ui.theme.DnD_sheetTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment


@Composable
fun MainScreen(
    toCharacterScreen: () -> Unit,
    toBack: () -> Unit,
    modifier: Modifier
){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = "Фон",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = toCharacterScreen,
            modifier = modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("На лист персонажа")
        }
    }


}

