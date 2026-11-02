package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.repository.ArtworkRepository
import com.renatosanz.artxplorer.domain.model.Artwork

class GetRandomArtworkUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(maxRandom: Long): Result<Artwork> {
        return repository.getRandomArtwork(maxRandom)
    }
}
