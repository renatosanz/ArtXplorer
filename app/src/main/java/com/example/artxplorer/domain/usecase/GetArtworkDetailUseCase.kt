package com.example.artxplorer.domain.usecase

import android.util.Log
import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.domain.model.Artwork
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.data.repository.GetArtworkResult

public class GetArtworkDetailUseCase(
    private val repository: ArtworkRepository,
) {
    suspend operator fun invoke(id: Long): Result<GetArtworkResult> {
        Log.d("GetArtworkDetailUseCase","retrieving artwork on GetArtworkDetailUseCase: $id")
        return repository.getArtwork(id)
    }
}