package com.example.dnd_sheet.Ktor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dnd_sheet.Models.ItemDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest

sealed interface ItemUiState {
    data object Idle : ItemUiState
    data object Loading : ItemUiState
    data class Success(val items: List<ItemDto>) : ItemUiState
    data class Error(val message: String) : ItemUiState
}

class ItemSearchViewModel : ViewModel() {
    private var allItems: List<ItemDto> = emptyList()
    private var isLoaded = false

    private var searchJob: Job? = null

    private val _searchQuery = MutableStateFlow("")
    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Idle)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _uiState.value = ItemUiState.Idle
                    } else {
                        triggerSearch(query)
                    }
                }
        }
    }

    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    private fun triggerSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchItems(query)
        }
    }

    private suspend fun searchItems(query: String) {
        if (!isLoaded) {
            _uiState.value = ItemUiState.Loading
            try {
                allItems = ItemApi.getAllItems()
                isLoaded = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = ItemUiState.Error("Ошибка сети: ${e.message}")
                return
            }
        }

        val filtered = allItems.filter {
            it.name.contains(query, ignoreCase = true)
        }.take(10)

        _uiState.value = ItemUiState.Success(filtered)
    }

    fun retry() {
        isLoaded = false
        triggerSearch(_searchQuery.value)
    }
}