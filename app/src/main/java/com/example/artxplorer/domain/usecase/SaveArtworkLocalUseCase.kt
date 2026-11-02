package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.domain.model.Artwork

class SaveArtworkLocalUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(artwork: ArtworkEntity): Result<Boolean> {
        return repository.saveArtwork(artwork)
    }
}