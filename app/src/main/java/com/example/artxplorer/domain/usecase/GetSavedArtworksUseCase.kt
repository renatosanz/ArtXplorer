package com.example.artxplorer.domain.usecase

import androidx.lifecycle.LiveData
import com.example.artxplorer.data.remote.api.HomePageArtworks
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.domain.model.Artwork

class GetSavedArtworksUseCase (
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(): Result<List<Artwork>> {
        return repository.getSavedArtwoks()
    }
}