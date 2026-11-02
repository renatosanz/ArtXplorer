package com.renatosanz.artxplorer.presentation.screens.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renatosanz.artxplorer.data.remote.api.HomePageArtworks
import com.renatosanz.artxplorer.data.remote.api.SearchArtworks
import com.renatosanz.artxplorer.domain.model.Artwork
import com.renatosanz.artxplorer.domain.usecase.GetHomePageUseCase
import com.renatosanz.artxplorer.domain.usecase.GetRandomArtworkUseCase
import com.renatosanz.artxplorer.domain.usecase.SearchArtworkUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val getHomePageUseCase: GetHomePageUseCase,
    private val getRandomImage: GetRandomArtworkUseCase,
    private val searchArtworkUseCase: SearchArtworkUseCase
) : ViewModel() {

    private val _homepage = MutableLiveData<HomePageArtworks>()
    val homepage: LiveData<HomePageArtworks> = _homepage

    private val _searchArtworksResults = MutableLiveData<SearchArtworks>()
    val searchArtworkResults: LiveData<SearchArtworks> = _searchArtworksResults

    private val _randomArtwork = MutableLiveData<Artwork>()
    val randomArtwork: LiveData<Artwork> = _randomArtwork

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var loadJob: Job? = null
    private var currentPage = 1

    fun loadHomePage(page: Int = 1) {
        if (loadJob?.isActive == true) return // debounce: never overlap home-page requests
        currentPage = page
        loadJob = viewModelScope.launch {
            _loading.value = true
            getHomePageUseCase(page).fold(onSuccess = { data ->
                _homepage.value = data
                if (_randomArtwork.value == null) { // ← solo la primera vez
                    loadRandomArtwork(data.pagination.total)
                }
            }, onFailure = { exception ->
                _error.value = "Couldn't load artworks. Please try again."
            })
            _loading.value = false
        }
    }

    fun refreshHomePage() = loadHomePage(currentPage)

    private fun loadRandomArtwork(maxRandom: Long) {
        viewModelScope.launch {
            getRandomImage(maxRandom).fold(
                onSuccess = { data -> _randomArtwork.value = data },
                onFailure = { exception -> _error.value = "Couldn't load an artwork. Please try again." })
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchArtworkUseCase(query).fold(
                onSuccess = { data -> _searchArtworksResults.value = data },
                onFailure = { e -> _error.value = "Search failed. Please try again." }
            )
        }
    }
}