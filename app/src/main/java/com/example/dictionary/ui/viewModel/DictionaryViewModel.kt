package com.example.dictionary.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.data.remote_model.DictionaryResponse
import com.example.dictionary.data.repository.DictionaryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(private val repository: DictionaryRepo) :
    ViewModel() {

    private val _uiState = MutableStateFlow(DictionaryScreen.UiState())
    val uiState = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")

    //we get the word and update to the viewmodel
    init {
        viewModelScope.launch {
            _query.debounce(1000)
                .filter { it.isNotBlank() }
                .collectLatest {
                    getMeaning(it)
                }
        }
    }

    //this passes the word from ui to the viewmodel
    fun updateWord(word: String) {
        _query.update { word }
    }


    //sending the word to the repository to get the meaning
    fun getMeaning(word: String) = viewModelScope.launch {
        _uiState.update { DictionaryScreen.UiState(isLoading = true) }
        val meaningResponse = repository.getMeaning(word)
        if (meaningResponse.isSuccess) {
            _uiState.update { DictionaryScreen.UiState(data = meaningResponse.getOrThrow()) }
        } else {
            _uiState.update { DictionaryScreen.UiState(error = meaningResponse.exceptionOrNull()?.message.toString()) }
        }
    }
}

object DictionaryScreen {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String = "",
        val data: DictionaryResponse? = null
    )
}
