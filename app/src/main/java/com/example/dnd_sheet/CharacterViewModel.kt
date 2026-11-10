package com.example.dnd_sheet

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class CharacterViewModel(private val repository: CharacterRepository) : ViewModel() {

    ///////////////////////////////////////////////////////////////////////////Персонаж
    private val _characters = mutableStateListOf<CharacterAttributes>()



    //private var nextId = 1L

    init {
        viewModelScope.launch {
            val saved = repository.loadCharacters()
            if (saved.isNotEmpty()) {
                _characters.addAll(saved)
            }
        }

        /*
        // Добавление стартовых персонажей
            _characters.add(CharacterAttributes(id = nextId++, nickname = "Арагорн", currentHp = 30))
            _characters.add(CharacterAttributes(id = nextId++, nickname = "Леголас", currentHp = 12))
            _characters.add(CharacterAttributes(id = nextId++, nickname = "Гэндальф", currentHp = 0, currentState = CharState.UNCONSCIOUS))
            _characters.add(CharacterAttributes(id = nextId++, nickname = "Боромир", currentHp = 0, currentState = CharState.DEAD))


            repeat(10) {
                _characters.add(
                    CharacterAttributes(
                        id = nextId++,
                        nickname = "Инкогнито",
                        characterClass = listOf("Воин", "Чародей", "Колдун", "Волшебник", "Варвар", "Паладин", "Изобретатель", "Следопыт", "Бард", "Плут", "Жрец").random(),
                        strength = (8..18).random(),
                        dexterity = (8..18).random(),
                        constitution = (8..18).random(),
                        intelligence = (8..18).random(),
                        wisdom = (8..18).random(),
                        charisma = (8..18).random(),
                        level = (1..20).random()
                    )
                )
            }
         */
    }

    private fun saveToDisk() {
        viewModelScope.launch {
            repository.saveCharacters(_characters)
        }
    }

    fun getListCharacters(): List<CharacterAttributes>{
        return _characters
    }
    fun getCharacter(id: Long): CharacterAttributes? {
        return _characters.find { it.id == id }
    }

    fun addCharacter() {
        _characters.add(
            CharacterAttributes()
        )
        saveToDisk()
    }

    fun removeCharacter(id: Long){
        _characters.removeAll {it.id == id}

        saveToDisk()
    }

    fun updateCharacter(id: Long, newCharacter: CharacterAttributes){
        val index = _characters.indexOfFirst { it.id == id }
        if (index != -1) {
            _characters[index] = newCharacter
        }

        saveToDisk()
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

        saveToDisk()
    }

    fun heal(id: Long, amount: Int){
        val character = getCharacter(id) ?: return
        if (character.currentState == CharState.DEAD) return

        val newHp = (character.currentHp + amount).coerceAtMost(maximumValue = character.maxHp)

        updateCharacter(id, character.copy(currentHp = newHp, currentState = CharState.HEALTHY))

        saveToDisk()

    }

    fun updateLevel(id: Long){
        val character = getCharacter(id) ?: return

        val newCharacter = character.copy(level = when (character.exp) {
            in 0..299 -> 1
            in 300..899 -> 2
            in 900..2699 -> 3
            in 2700..6499 -> 4
            in 6500..13999 -> 5
            in 14000..22999 -> 6
            in 23000..33999 -> 7
            in 34000..47999 -> 8
            in 48000..63999 -> 9
            in 64000..84999 -> 10
            in 85000..99999 -> 11
            in 100000..119999 -> 12
            in 120000..139999 -> 13
            in 140000..164999 -> 14
            in 165000..194999 -> 15
            in 195000..224999 -> 16
            in 225000..264999 -> 17
            in 265000..304999 -> 18
            in 305000..354999 -> 19
            else -> 20
        })

        updateCharacter(id, newCharacter)

        saveToDisk()
    }

    fun updateExp(id: Long, newExp: Int) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(exp = newExp))
        updateLevel(id)

        saveToDisk()
    }

    fun updateClass(id: Long, newClass: String) {
        val character = getCharacter(id) ?: return

        updateCharacter(id, character.copy(characterClass = newClass))

        saveToDisk()
    }

    fun updateNickname(id: Long, newNickname: String) {
        val character = getCharacter(id) ?: return

        updateCharacter(id, character.copy(nickname = newNickname))

        saveToDisk()
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

        saveToDisk()
    }

    fun updateAvatar(id: Long, newUri: String?) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(avatarUri = newUri))

        saveToDisk()
    }

    fun reset(id: Long) {
        val character = getCharacter(id) ?: return

        val newCharacter = character.copy(
            currentHp = character.maxHp,
            currentState = CharState.HEALTHY
        )
        updateCharacter(id, newCharacter)

        saveToDisk()
    }

    ////////////////////////////////////////////////////////////////////////////////Инвентарь

    fun addItem(id: Long, item: Items){
        val character = getCharacter(id) ?: return
        val newInventory = character.inventory + item
        updateCharacter(id, character.copy(inventory = newInventory))

        saveToDisk()
    }

    fun removeItem(id: Long, itemId: Long){
        val character = getCharacter(id) ?: return
        val newInventory = character.inventory.filterNot { it.id == itemId }
        updateCharacter(id, character.copy(inventory = newInventory))

        saveToDisk()
    }

}