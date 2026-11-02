package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity
import com.renatosanz.artxplorer.data.repository.ArtworkRepository
import com.renatosanz.artxplorer.domain.model.Artwork

class SaveArtworkLocalUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(artwork: ArtworkEntity): Result<Boolean> {
        return repository.saveArtwork(artwork)
    }
}