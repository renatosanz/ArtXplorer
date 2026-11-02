package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity
import com.renatosanz.artxplorer.data.repository.ArtworkRepository
import com.renatosanz.artxplorer.data.repository.GetArtworkResult
import com.renatosanz.artxplorer.domain.model.Artwork
import com.renatosanz.artxplorer.logDebug

class DeleteArtworkLocalUseCase(
    private val repository: ArtworkRepository,

    ) {
    suspend operator fun invoke(artworkEntity: ArtworkEntity) {
        logDebug("DeleteArtworkLocalUseCase","deleting artwork: $artworkEntity")
        repository.deleteArtwork(artworkEntity)
    }
}