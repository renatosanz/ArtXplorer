package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.domain.model.Artwork

class GetArtistRecommendationsUseCase(
    private val repository: ArtworkRepository,
) {
    suspend operator fun invoke(
        artistId: Long,
        excludeArtworkId: Long
    ): Result<List<Artwork>> = repository.getRecommendations(artistId, excludeArtworkId)
}
