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
data class CharacterScreenRoute(val characterId: Long) : NavKey

@Serializable
data class InventoryScreenRoute(val characterId: Long) : NavKey

@Composable
fun AppNavigation(characterViewModel: CharacterViewModel, modifier: Modifier) {
    val backStack = remember { mutableStateListOf<NavKey>(MainScreenRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<MainScreenRoute> {
                MainScreen(
                    characters = characterViewModel.characters,
                    onAddCharacter = { characterViewModel.addCharacter() },
                    onCharacterClick = { id -> backStack.add(CharacterScreenRoute(id)) },
                    onRemoveCharacter = { id -> characterViewModel.removeCharacter(id) },
                    modifier = modifier
                )
            }

            entry<CharacterScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null) {
                    characterScreen(
                        character = character,
                        takeDamage = { characterViewModel.takeDamage(route.characterId, it) },
                        heal = { characterViewModel.heal(route.characterId, it) },
                        updateStat = { stat, value -> characterViewModel.updateStat(route.characterId, stat, value) },
                        reset = { characterViewModel.reset(route.characterId) },
                        toInventory = {id -> backStack.add(InventoryScreenRoute(id)) },
                        toBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }
            }

            entry<InventoryScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    Inventory(
                        character = character,
                        addItem = { item -> characterViewModel.addItem(route.characterId, item) },
                        removeItem = { itemId -> characterViewModel.removeItem(route.characterId, itemId) },
                        toBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }

            }
        }
    )
}