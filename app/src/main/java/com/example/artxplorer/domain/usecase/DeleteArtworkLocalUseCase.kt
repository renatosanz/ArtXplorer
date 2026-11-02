package com.example.artxplorer.domain.usecase

import android.util.Log
import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.data.repository.GetArtworkResult
import com.example.artxplorer.domain.model.Artwork

class DeleteArtworkLocalUseCase(
    private val repository: ArtworkRepository,

    ) {
    suspend operator fun invoke(artworkEntity: ArtworkEntity) {
        Log.d("DeleteArtworkLocalUseCase","deleting artwork: $artworkEntity")
        repository.deleteArtwork(artworkEntity)
    }
}