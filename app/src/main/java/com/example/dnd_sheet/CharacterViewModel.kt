package com.example.dnd_sheet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel



class CharacterViewModel : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////Персонаж
    private val _characters = mutableStateListOf<CharacterAttributes>()

    val characters: List<CharacterAttributes> get() = _characters

    fun getCharacter(id: Long): CharacterAttributes? {
        return _characters.find { it.id == id }
    }

    fun addCharacter() {
        _characters.add(
            CharacterAttributes()
        )
    }

    fun removeCharacter(id: Long){
        _characters.removeAll {it.id == id}
    }

    fun updateCharacter(id: Long, newCharacter: CharacterAttributes){
        val index = _characters.indexOfFirst { it.id == id }
        if (index != -1) {
            _characters[index] = newCharacter
        }
    }

    fun takeDamage(id: Long, amount: Int){
        val character = getCharacter(id) ?: return

        val newHp = (character.currentHp - amount).coerceAtLeast(minimumValue = 0)

        val newState = when {
            newHp > 0 -> character.currentState
            (character.currentState == CharState.HEALTHY) && (newHp == 0) -> CharState.UNCONSCIOUS
            else -> CharState.DEAD
        }

        updateCharacter(id, character.copy(currentHp = newHp, currentState = newState))
    }

    fun heal(id: Long, amount: Int){
        val character = getCharacter(id) ?: return
        if (character.currentState == CharState.DEAD) return

        val newHp = (character.currentHp + amount).coerceAtMost(maximumValue = character.maxHp)

        updateCharacter(id, character.copy(currentHp = newHp, currentState = CharState.HEALTHY))

    }

    fun updateStat(id: Long, attributeName: String, value: Int) {
        val character = getCharacter(id) ?: return

        val newCharacter = when (attributeName) {
            "СИЛ" -> character.copy(strength = value)
            "ЛВК" -> character.copy(dexterity = value)
            "ТЕЛ" -> character.copy(constitution = value)
            "ИНТ" -> character.copy(intelligence = value)
            "МДР" -> character.copy(wisdom = value)
            "ХАР" -> character.copy(charisma = value)
            else -> character
        }
        updateCharacter(id, newCharacter)
    }


    fun reset(id: Long) {
        val character = getCharacter(id) ?: return

        val newCharacter = character.copy(
            currentHp = character.maxHp,
            currentState = CharState.HEALTHY
        )
        updateCharacter(id, newCharacter)
    }

    ////////////////////////////////////////////////////////////////////////////////Инвентарь

    fun addItem(id: Long, item: Items){
        val character = getCharacter(id) ?: return
        val newInventory = character.inventory + item
        updateCharacter(id, character.copy(inventory = newInventory))
    }

    fun removeItem(id: Long, itemId: Long){
        val character = getCharacter(id) ?: return
        val newInventory = character.inventory.filterNot { it.id == itemId }
        updateCharacter(id, character.copy(inventory = newInventory))
    }

}