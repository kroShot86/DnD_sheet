package com.example.dnd_sheet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel



class CharacterViewModel : ViewModel() {
    var character by mutableStateOf(CharacterAttributes())
        private set


    fun takeDamage(amount: Int){
        character = character.copy(currentHp = (character.currentHp - amount).coerceAtLeast(minimumValue = 0))
        if ( (character.currentHp == 0) && (character.currentState == CharState.HEALTHY) ) {
            character = character.copy(currentState = CharState.UNCONSCIOUS)
        }
        else if ((character.currentHp == 0) && (character.currentState == CharState.UNCONSCIOUS)){
            character = character.copy(currentState = CharState.DEAD)
        }
    }

    fun heal(amount: Int){
        if (character.currentState == CharState.DEAD) return
        character = character.copy(currentHp = (character.currentHp + amount).coerceAtMost(maximumValue = character.maxHp))
        if (character.currentState == CharState.UNCONSCIOUS){
            character = character.copy(currentState = CharState.HEALTHY)
        }
    }

    fun updateStat(attributeName: String, value: Int){
        character = when (attributeName){
            "СИЛ" -> character.copy(strength = value)
            "ЛВК" -> character.copy(dexterity = value)
            "ТЕЛ" -> character.copy(constitution = value)
            "ИНТ" -> character.copy(intelligence = value)
            "МДР" -> character.copy(wisdom = value)
            "ХАР" -> character.copy(charisma = value)
            else -> character
        }
    }


    fun reset() {
        character = CharacterAttributes()
    }

}