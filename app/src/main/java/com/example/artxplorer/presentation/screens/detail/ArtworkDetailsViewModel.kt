package com.example.artxplorer.presentation.screens.detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.data.repository.GetArtworkResult
import com.example.artxplorer.domain.model.Artwork
import com.example.artxplorer.domain.usecase.DeleteArtworkLocalUseCase
import com.example.artxplorer.domain.usecase.GetArtistRecommendationsUseCase
import com.example.artxplorer.domain.usecase.GetArtworkDetailUseCase
import com.example.artxplorer.domain.usecase.SaveArtworkLocalUseCase
import com.example.artxplorer.data.common.saveImageToCache
import com.example.artxplorer.data.common.saveImageToGallery
import kotlinx.coroutines.launch

class ArtworkDetailsViewModel(
    private val getArtworkDetailUseCase: GetArtworkDetailUseCase,
    private val saveArtworkLocalUseCase: SaveArtworkLocalUseCase,
    private val deleteArtworkLocalUseCase: DeleteArtworkLocalUseCase,
    private val getArtistRecommendationsUseCase: GetArtistRecommendationsUseCase
) : ViewModel() {

    private val _artworkData = MutableLiveData<ArtworkEntity>()
    val artwork: LiveData<ArtworkEntity> = _artworkData

    private val _recommendations = MutableLiveData<List<Artwork>>()
    val recommendations: LiveData<List<Artwork>> = _recommendations

    private val _isSaved = MutableLiveData<Boolean>()
    val isSaved: LiveData<Boolean> = _isSaved

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _msgToast = MutableLiveData<String>()
    val msgToast: LiveData<String> = _msgToast

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadArtworkDetails(artworkId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _recommendations.value = emptyList()
            var artistId: Long? = null
            getArtworkDetailUseCase(artworkId).fold(onSuccess = {
                print("artwork recieved: $it")
                when (it) {
                    is GetArtworkResult.FoundOnLocal -> {
                        _isSaved.value = true
                        _artworkData.value = it.artworkEntity
                        artistId = it.artworkEntity.artistId
                    }

                    is GetArtworkResult.FoundOnApi -> {
                        _isSaved.value = false
                        _artworkData.value = it.artwork.toEntity()
                        artistId = it.artwork.artistId
                    }

                    else -> {
                        _error.value = "Respuesta de fuente desconocida"
                    }
                }
            }, onFailure = {
                _error.value = it.message
            })
            _loading.value = false
            // Secondary content: failures just leave the section hidden.
            artistId?.let { id ->
                getArtistRecommendationsUseCase(id, artworkId)
                    .onSuccess { _recommendations.value = it }
            }
        }
    }

    fun saveArtworkLocal(context: Context, imageUrl: String) {
        val currentArtwork = _artworkData.value
        if (currentArtwork != null) {
            viewModelScope.launch {
                saveImageToCache(context, imageUrl, currentArtwork.title).fold(onSuccess = {
                    saveArtworkLocalUseCase(currentArtwork.copy(localImagePath = it.toString())).fold(
                        onSuccess = { saved ->
                            _isSaved.value = saved
                            _msgToast.value = "Artwork Saved"
                        },
                        onFailure = { _error.value = it.message })
                }, onFailure = { _error.value = it.message })
            }
        } else {
            _error.value = "No hay datos de la obra para guardar todavía."
        }
    }

    fun deleteLocally() {
        val currentArtwork = _artworkData.value
        if (currentArtwork != null) {
            viewModelScope.launch {
                deleteArtworkLocalUseCase(currentArtwork)
                _msgToast.value = "Artwork deleted ${currentArtwork.title}"
            }
        } else {
            _error.value = "No hay datos de la obra para guardar todavía."
        }
    }

    fun saveArtworkToDevice(context: Context, imageUrl: String) {
        val currentArtwork = _artworkData.value
        if (currentArtwork != null) {
            viewModelScope.launch {
                saveImageToGallery(context, imageUrl, currentArtwork.title)
                _msgToast.value = "Artwork saved on Pictures"
            }
        } else {
            _error.value = "No hay datos de la obra para guardar todavía."
        }

    }
}