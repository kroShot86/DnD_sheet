package com.example.dnd_sheet

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dnd_sheet.Models.CharState
import com.example.dnd_sheet.Models.CharacterAttributes
import com.example.dnd_sheet.Models.Items
import com.example.dnd_sheet.Models.ProficiencyLevel
import com.example.dnd_sheet.Models.Spell
import com.example.dnd_sheet.Models.SpellLevel
import com.example.dnd_sheet.Models.SpellSlots
import com.example.dnd_sheet.Models.StatType
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

    }

    fun heal(id: Long, amount: Int){
        val character = getCharacter(id) ?: return
        if (character.currentState == CharState.DEAD) return

        val newHp = (character.currentHp + amount).coerceAtMost(maximumValue = character.maxHp)


        updateCharacter(id, character.copy(currentHp = newHp, currentState = CharState.HEALTHY, deathSavesSuccesses = 0, deathSavesFailures = 0))

    }

    fun updateDeathSave(characterId: Long, isSuccess: Boolean, delta: Int) {
        val character = getCharacter(characterId) ?: return

        var newSuccesses = character.deathSavesSuccesses
        var newFailures = character.deathSavesFailures

        if (isSuccess) {
            newSuccesses = (newSuccesses + delta).coerceIn(0, 3)
        } else {
            newFailures = (newFailures + delta).coerceIn(0, 3)
        }

        updateCharacter(
            characterId,
            character.copy(
                deathSavesSuccesses = newSuccesses,
                deathSavesFailures = newFailures
            )
        )
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

    }

    fun updateExp(id: Long, newExp: Int) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(exp = newExp))
        updateLevel(id)
    }

    fun updateClass(id: Long, newClass: String) {
        val character = getCharacter(id) ?: return

        updateCharacter(id, character.copy(characterClass = newClass))

    }

    fun updateNickname(id: Long, newNickname: String) {
        val character = getCharacter(id) ?: return

        updateCharacter(id, character.copy(nickname = newNickname))

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

    fun updateAvatar(id: Long, newUri: String?) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(avatarUri = newUri))

    }

    fun reset(id: Long) {
        val character = getCharacter(id) ?: return

        val newCharacter = character.copy(
            currentHp = character.maxHp,
            currentState = CharState.HEALTHY,
            deathSavesSuccesses = 0,
            deathSavesFailures = 0,
            currentCountDice = character.maxCountDice
        )
        updateCharacter(id, newCharacter)

    }

    fun updateMaxHp(id: Long, newMaxHp: Int) {
        val character = getCharacter(id) ?: return

        if (newMaxHp > 0){
            updateCharacter(id, character.copy(maxHp = newMaxHp))
        } else {
            return
        }

    }

    fun updateHitDice(characterId: Long, delta: Int) {
        val character = getCharacter(characterId) ?: return
        val newCurrentCountDice = (character.currentCountDice - delta).coerceIn(0, character.level)

        updateCharacter(characterId, character.copy(currentCountDice = newCurrentCountDice))
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

    fun changeItemCount(characterId: Long, itemId: Long, delta: Int) {
        val character = getCharacter(characterId) ?: return
        val updatedInventory = character.inventory.map { item ->
            if (item.id == itemId) {
                val newCount = (item.count + delta).coerceAtLeast(0)
                item.copy(count = newCount)
            } else {
                item
            }
        }

        updateCharacter(characterId, character.copy(inventory = updatedInventory))
    }

    fun updateItem(characterId: Long, updatedItem: Items) {
        val character = getCharacter(characterId) ?: return
        val newInventory = character.inventory.map { item ->
            if (item.id == updatedItem.id) updatedItem else item
        }
        updateCharacter(characterId, character.copy(inventory = newInventory))

    }


    //Навыки
    fun toggleSkill(characterId: Long, skillName: String) {
        val character = getCharacter(characterId) ?: return
        val updatedSkills = character.skills.map { skill ->
            if (skill.name == skillName) {
                val newLevel = when (skill.proficiency) {
                    ProficiencyLevel.NONE -> ProficiencyLevel.PROFICIENT
                    ProficiencyLevel.PROFICIENT -> ProficiencyLevel.EXPERTISE
                    ProficiencyLevel.EXPERTISE -> ProficiencyLevel.NONE
                }
                skill.copy(proficiency = newLevel)
            } else {
                skill
            }
        }.sortedBy { it.name }
        updateCharacter(characterId, character.copy(skills = updatedSkills))
    }

    fun updateSkillExtraBonus(characterId: Long, skillName: String, bonus: Int) {
        val character = getCharacter(characterId) ?: return
        val updatedSkills = character.skills.map { skill ->
            if (skill.name == skillName) skill.copy(extraBonus = bonus) else skill
        }.sortedBy { it.name }
        updateCharacter(characterId, character.copy(skills = updatedSkills))
    }

    fun toggleSave(characterId: Long, stat: StatType) {
        val character = getCharacter(characterId) ?: return
        val currentSaves = character.proficientSaves.toMutableList()
        if (currentSaves.contains(stat)) currentSaves.remove(stat) else currentSaves.add(stat)
        updateCharacter(characterId, character.copy(proficientSaves = currentSaves))
    }

    fun updateSaveExtraBonus(characterId: Long, stat: StatType, bonus: Int) {
        val character = getCharacter(characterId) ?: return
        val newBonuses = character.saveExtraBonuses.toMutableMap()
        newBonuses[stat] = bonus
        updateCharacter(characterId, character.copy(saveExtraBonuses = newBonuses))
    }


    //История
    fun updateAlignment(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(alignment = value))
    }

    fun updateBackstory(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(backstory = value))
    }

    fun updateIdeals(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(ideals = value))
    }

    fun updateBonds(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(bonds = value))
    }

    fun updateFlaws(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(flaws = value))
    }

    fun updateNotes(id: Long, value: String) {
        val character = getCharacter(id) ?: return
        updateCharacter(id, character.copy(notes = value))
    }

    // Магия
    fun addSpell(characterId: Long, spell: Spell) {
        val character = getCharacter(characterId) ?: return
        updateCharacter(characterId, character.copy(spells = character.spells + spell))
    }

    fun updateSpell(characterId: Long, updatedSpell: Spell) {
        val character = getCharacter(characterId) ?: return
        val newSpells = character.spells.map { if (it.id == updatedSpell.id) updatedSpell else it }
        updateCharacter(characterId, character.copy(spells = newSpells))
    }

    fun removeSpell(characterId: Long, spellId: Long) {
        val character = getCharacter(characterId) ?: return
        val newSpells = character.spells.filterNot { it.id == spellId }
        updateCharacter(characterId, character.copy(spells = newSpells))
    }

    fun toggleSpellPrepared(characterId: Long, spellId: Long) {
        val character = getCharacter(characterId) ?: return
        val newSpells = character.spells.map {
            if (it.id == spellId) it.copy(isPrepared = !it.isPrepared) else it
        }
        updateCharacter(characterId, character.copy(spells = newSpells))
    }

    fun updateSpellSlot(characterId: Long, level: SpellLevel, newMax: Int? = null, newUsed: Int? = null) {
        val character = getCharacter(characterId) ?: return
        val currentSlots = character.spellSlots.toMutableMap()
        val current = currentSlots[level] ?: SpellSlots()
        currentSlots[level] = current.copy(
            max = newMax ?: current.max,
            used = newUsed ?: current.used
        )
        updateCharacter(characterId, character.copy(spellSlots = currentSlots))
    }

    fun resetSpellSlots(characterId: Long) {
        val character = getCharacter(characterId) ?: return
        val resetSlots = character.spellSlots.mapValues { it.value.copy(used = 0) }
        updateCharacter(characterId, character.copy(spellSlots = resetSlots))
    }


    //Умения
    fun updateProficiencies(characterId: Long, text: String) {
        val character = getCharacter(characterId) ?: return
        updateCharacter(characterId, character.copy(proficiencies = text))
    }

    fun addFeature(characterId: Long, feature: Feature) {
        val character = getCharacter(characterId) ?: return
        updateCharacter(characterId, character.copy(features = character.features + feature))
    }

    fun updateFeature(characterId: Long, feature: Feature) {
        val character = getCharacter(characterId) ?: return
        val newFeatures = character.features.map { if (it.id == feature.id) feature else it }
        updateCharacter(characterId, character.copy(features = newFeatures))
    }

    fun removeFeature(characterId: Long, featureId: Long) {
        val character = getCharacter(characterId) ?: return
        updateCharacter(characterId, character.copy(features = character.features.filterNot { it.id == featureId }))
    }

}