package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.repository.ArtworkRepository

class ClearSavedArtworksUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.clearSavedArtworks()
    }
}
