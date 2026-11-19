package com.example.dnd_sheet.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dnd_sheet.Models.SpellDto
import com.example.dnd_sheet.Network.KtorClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

sealed interface SpellUiState {
    data object Idle : SpellUiState
    data object Loading : SpellUiState
    data class Success(val spells: List<SpellDto>) : SpellUiState
    data class Error(val message: String) : SpellUiState
}

class SpellSearchViewModel : ViewModel() {
    private var allSpells: List<SpellDto> = emptyList()
    private var isLoaded = false

    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow<SpellUiState>(SpellUiState.Idle)
    val uiState: StateFlow<SpellUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        _uiState.value = SpellUiState.Idle
                    } else {
                        searchSpells(query)
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchSpells(query: String) {
        if (!isLoaded) {
            _uiState.value = SpellUiState.Loading
            try {
                allSpells = KtorClient.getAllSpells()
                isLoaded = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = SpellUiState.Error("Ошибка сети: ${e.message}")
                return
            }
        }

        val filtered = allSpells.filter {
            it.nameRu.contains(query, ignoreCase = true) ||
                    it.nameEn.contains(query, ignoreCase = true)
        }.take(5)

        _uiState.value = SpellUiState.Success(filtered)
    }

    fun retry() {
        isLoaded = false
        viewModelScope.launch { searchSpells(_searchQuery.value) }
    }
}