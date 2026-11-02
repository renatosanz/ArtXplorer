package com.renatosanz.artxplorer.presentation.screens.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatosanz.artxplorer.domain.usecase.ClearSavedArtworksUseCase
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(
    private val context: Context,
    private val clearSavedArtworksUseCase: ClearSavedArtworksUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _cleared = MutableLiveData<Boolean>()
    val cleared: LiveData<Boolean> = _cleared

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun clearSavedArtworks() {
        if (_loading.value == true) return
        viewModelScope.launch {
            _loading.value = true
            clearSavedArtworksUseCase().fold(onSuccess = {
                File(context.filesDir, "artworks").deleteRecursively()
                _cleared.value = true
            }, onFailure = { exception ->
                _error.value = exception.message
            })
            _loading.value = false
        }
    }
}
