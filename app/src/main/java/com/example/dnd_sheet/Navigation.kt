package com.example.dnd_sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.entryProvider
import com.example.dnd_sheet.Screens.Inventory
import com.example.dnd_sheet.Screens.MainScreen
import com.example.dnd_sheet.Screens.characterScreen

@Serializable
data object MainScreenRoute : NavKey

@Serializable
data object CharacterScreenRoute : NavKey

@Serializable
data object InventoryRoute : NavKey

@Composable
fun AppNavigation(stateHolder: CharacterStateHolder, modifier: Modifier) {
    val backStack = remember { mutableStateListOf<NavKey>(MainScreenRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },

        entryProvider = entryProvider {

            entry<MainScreenRoute> {
                MainScreen(
                    toCharacterScreen = { backStack.add(CharacterScreenRoute) },
                    toBack = { backStack.removeLastOrNull() },
                    modifier = modifier
                )
            }

            entry <CharacterScreenRoute> {
                characterScreen(
                    character = stateHolder.character,
                    takeDamage = { stateHolder.takeDamage(it) },
                    heal = { stateHolder.heal(it) },
                    updateStat = { stat, value -> stateHolder.updateStat(stat, value) },
                    reset = { stateHolder.reset() },
                    toInventory = { backStack.add(InventoryRoute) },
                    toBack = { backStack.removeLastOrNull() },
                    modifier = modifier
                )
            }

            entry<InventoryRoute> {
                Inventory(toBack = { backStack.removeLastOrNull() },
                    modifier = modifier
                )

            }
        }
    )
}