package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.repository.ArtworkRepository

class ClearSavedArtworksUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.clearSavedArtworks()
    }
}
