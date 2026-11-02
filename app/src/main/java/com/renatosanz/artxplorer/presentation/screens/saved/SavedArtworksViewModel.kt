package com.renatosanz.artxplorer.presentation.screens.saved

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity
import com.renatosanz.artxplorer.domain.model.Artwork
import com.renatosanz.artxplorer.domain.usecase.DeleteArtworkLocalUseCase
import com.renatosanz.artxplorer.domain.usecase.GetSavedArtworksUseCase
import kotlinx.coroutines.launch

class SavedArtworksViewModel(
    private val getSavedArtworksUseCase: GetSavedArtworksUseCase,
    private val deleteArtworkLocalUseCase: DeleteArtworkLocalUseCase
) :
    ViewModel() {
    private val _savedArtworks = MutableLiveData<List<Artwork>>()
    val savedArtworks: LiveData<List<Artwork>> = _savedArtworks

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadSavedArtworks() {
        viewModelScope.launch {
            _loading.value = true
            getSavedArtworksUseCase().fold(onSuccess = { data ->
                Log.d("SavedArtworksViewModel", "saved artworks: $data")
                _savedArtworks.value = data
            }, onFailure = { exception ->
                _error.value = exception.message
            })
            _loading.value = false
        }
    }

    fun deleteArtwork(artwork: ArtworkEntity) {
        viewModelScope.launch {
            _loading.value = true
            deleteArtworkLocalUseCase(artwork)
            loadSavedArtworks()
            _loading.value = false
        }
    }
}