package com.renatosanz.artxplorer.domain.usecase

import androidx.lifecycle.LiveData
import com.renatosanz.artxplorer.data.remote.api.HomePageArtworks
import com.renatosanz.artxplorer.data.repository.ArtworkRepository
import com.renatosanz.artxplorer.domain.model.Artwork

class GetSavedArtworksUseCase (
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(): Result<List<Artwork>> {
        return repository.getSavedArtwoks()
    }
}