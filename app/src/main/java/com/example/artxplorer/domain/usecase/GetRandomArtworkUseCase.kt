package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.domain.model.Artwork

class GetRandomArtworkUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(maxRandom: Long): Result<Artwork> {
        return repository.getRandomArtwork(maxRandom)
    }
}
