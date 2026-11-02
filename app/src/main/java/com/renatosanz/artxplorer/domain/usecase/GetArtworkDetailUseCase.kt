package com.renatosanz.artxplorer.domain.usecase

import android.util.Log
import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity
import com.renatosanz.artxplorer.domain.model.Artwork
import com.renatosanz.artxplorer.data.repository.ArtworkRepository
import com.renatosanz.artxplorer.data.repository.GetArtworkResult

public class GetArtworkDetailUseCase(
    private val repository: ArtworkRepository,
) {
    suspend operator fun invoke(id: Long): Result<GetArtworkResult> {
        Log.d("GetArtworkDetailUseCase","retrieving artwork on GetArtworkDetailUseCase: $id")
        return repository.getArtwork(id)
    }
}