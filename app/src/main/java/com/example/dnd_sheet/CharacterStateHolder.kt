package com.example.dnd_sheet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.Saver


val CharacterStateHolderSaver = listSaver<CharacterStateHolder, Any>(
    save = { stateHolder ->
        val char = stateHolder.character
        listOf(
            char.strength, char.dexterity, char.constitution,
            char.intelligence, char.wisdom, char.charisma,
            char.currentHp, char.currentState.ordinal
        )
    },
    restore = { list ->
        val char = CharacterAttributes(
            strength = list[0] as Int,
            dexterity = list[1] as Int,
            constitution = list[2] as Int,
            intelligence = list[3] as Int,
            wisdom = list[4] as Int,
            charisma = list[5] as Int,
            currentHp = list[6] as Int,
            currentState = CharState.entries[list[7] as Int]
        )
        CharacterStateHolder(char)
    }
)

class CharacterStateHolder (initialCharacter: CharacterAttributes = CharacterAttributes()) {
    var character by mutableStateOf(initialCharacter)
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
        if (character.currentState == CharState.UNCONSCIOUS){ // Здесь проверка на количество хп не нужна, так как эта проверка физически не может запуститься, если у персонажа 0 хп (на строке выше мы повышаем здоровье)
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


    fun reset(){
        character = CharacterAttributes()
    }

}