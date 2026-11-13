package com.example.dnd_sheet

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.dnd_sheet.Models.SpellSlots
import com.example.dnd_sheet.Screens.InventoryScreen
import com.example.dnd_sheet.Screens.MainScreen
import com.example.dnd_sheet.Screens.CharacterScreen
import com.example.dnd_sheet.Screens.FeaturesScreen
import com.example.dnd_sheet.Screens.HistoryScreen
import com.example.dnd_sheet.Screens.MagikScreen
import com.example.dnd_sheet.Screens.SkillsScreen


@Serializable
data object MainScreenRoute : NavKey

@Serializable
data class CharacterScreenRoute(val characterId: Long) : NavKey

@Serializable
data class InventoryScreenRoute(val characterId: Long) : NavKey

@Serializable
data class SkillsScreenRoute(val characterId: Long) : NavKey

@Serializable
data class HistoryScreenRoute(val characterId: Long) : NavKey

@Serializable
data class MagikScreenRoute(val characterId: Long) : NavKey

@Serializable
data class FeaturesScreenRoute(val characterId: Long) : NavKey

@Composable
fun AppNavigation(characterViewModel: CharacterViewModel, modifier: Modifier) {
    val backStack = rememberNavBackStack(MainScreenRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<MainScreenRoute> {
                MainScreen(
                    characters = characterViewModel.getListCharacters(),
                    onAddCharacter = { characterViewModel.addCharacter() },
                    onCharacterClick = { id -> backStack.add(CharacterScreenRoute(id)) },
                    onRemoveCharacter = { id -> characterViewModel.removeCharacter(id) }
                )
            }

            entry<CharacterScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null) {
                    CharacterScreen(
                        character = character,
                        takeDamage = { characterViewModel.takeDamage(route.characterId, it) },
                        heal = { characterViewModel.heal(route.characterId, it) },
                        updateDeathSave = { isSuccess, delta -> characterViewModel.updateDeathSave(route.characterId, isSuccess, delta) },
                        updateStat = { stat, value -> characterViewModel.updateStat(route.characterId, stat, value) },
                        updateExp = { newExp -> characterViewModel.updateExp(route.characterId, newExp) },
                        updateClass = { newClass -> characterViewModel.updateClass(route.characterId, newClass) },
                        updateNickname = { newNickname -> characterViewModel.updateNickname(route.characterId, newNickname) },
                        updateAvatar = { uri -> characterViewModel.updateAvatar(route.characterId, uri) },
                        reset = { characterViewModel.reset(route.characterId) },
                        updateMaxHp = { newMaxHp -> characterViewModel.updateMaxHp(route.characterId, newMaxHp) },
                        updateHitDice = { delta -> characterViewModel.updateHitDice(route.characterId, delta) },
                        toInventory = { id -> backStack.add(InventoryScreenRoute(id)) },
                        toSkills = { id -> backStack.add(SkillsScreenRoute(id)) },
                        toHistory = { id -> backStack.add(HistoryScreenRoute(id)) },
                        toMagik = { id -> backStack.add(MagikScreenRoute(id)) },
                        toFeatures = { id -> backStack.add((FeaturesScreenRoute(id))) },
                        toBack = { backStack.removeLastOrNull() },
                        modifier = modifier
                    )
                }
            }

            entry<InventoryScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    InventoryScreen(
                        character = character,
                        addItem = { item -> characterViewModel.addItem(route.characterId, item) },
                        updateItem = { item -> characterViewModel.updateItem(route.characterId, item) },
                        removeItem = { itemId -> characterViewModel.removeItem(route.characterId, itemId) },
                        changeCount = { itemId, delta -> characterViewModel.changeItemCount(route.characterId, itemId, delta) },
                        toBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry <SkillsScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    SkillsScreen(
                        character = character,
                        onToggleSkill = {skillName -> characterViewModel.toggleSkill(route.characterId, skillName)},
                        onToggleSave = { statName -> characterViewModel.toggleSave(route.characterId, statName) },
                        onUpdateSkillBonus = { skillName, bonus -> characterViewModel.updateSkillExtraBonus(route.characterId, skillName, bonus) },
                        onUpdateSaveBonus = { stat, bonus -> characterViewModel.updateSaveExtraBonus(route.characterId, stat, bonus) },
                        toBack = { backStack.removeLastOrNull() }
                    )
                }
            }

            entry <HistoryScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    HistoryScreen(
                        character = character,
                        onUpdateAlignment = { alignment -> characterViewModel.updateAlignment(route.characterId, alignment) },
                        onUpdateBackstory = { backstory -> characterViewModel.updateBackstory(route.characterId, backstory) },
                        onUpdateIdeals = { ideals -> characterViewModel.updateIdeals(route.characterId, ideals) },
                        onUpdateBonds = { bonds -> characterViewModel.updateBonds(route.characterId, bonds) },
                        onUpdateFlaws = { flaws -> characterViewModel.updateFlaws(route.characterId, flaws) },
                        onUpdateNotes = { notes -> characterViewModel.updateNotes(route.characterId, notes) },
                        toBack = { backStack.removeLastOrNull() },
                    )
                }
            }

            entry <MagikScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    MagikScreen(
                        character = character,
                        onAddSpell = { spell -> characterViewModel.addSpell(route.characterId, spell) },
                        onUpdateSpell = { spell -> characterViewModel.updateSpell(route.characterId, spell) },
                        onRemoveSpell = { spellId -> characterViewModel.removeSpell(route.characterId, spellId) },
                        onTogglePrepared = { spellId -> characterViewModel.toggleSpellPrepared(route.characterId, spellId) },
                        onUpdateSlot = { level, max -> characterViewModel.updateSpellSlot(route.characterId, level, newMax = max) },
                        onUseSlot = { level ->
                            val current = character.spellSlots[level] ?: SpellSlots()
                            if (current.used < current.max) {
                                characterViewModel.updateSpellSlot(route.characterId, level, newUsed = current.used + 1)
                            }
                        },
                        onRestoreSlot = { level ->
                            val current = character.spellSlots[level] ?: SpellSlots()
                            if (current.used > 0) {
                                characterViewModel.updateSpellSlot(route.characterId, level, newUsed = current.used - 1)
                            }
                        },
                        onLongRest = { characterViewModel.resetSpellSlots(route.characterId) },
                        toBack = { backStack.removeLastOrNull() }
                    )
                }
            }


            entry <FeaturesScreenRoute> { route ->
                val character = characterViewModel.getCharacter(route.characterId)
                if (character != null){
                    FeaturesScreen(
                        character = character,
                        onUpdateProficiencies = { text ->
                            characterViewModel.updateProficiencies(route.characterId, text)
                        },
                        onAddFeature = { feature ->
                            characterViewModel.addFeature(route.characterId, feature)
                        },
                        onUpdateFeature = { feature ->
                            characterViewModel.updateFeature(route.characterId, feature)
                        },
                        onRemoveFeature = { featureId ->
                            characterViewModel.removeFeature(route.characterId, featureId)
                        },
                        toBack = { backStack.removeLastOrNull() },
                    )
                }
            }


        }
    )
}