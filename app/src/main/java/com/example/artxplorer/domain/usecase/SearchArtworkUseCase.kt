package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.remote.api.SearchArtworks
import com.example.artxplorer.data.repository.ArtworkRepository

class SearchArtworkUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(query:String): Result<SearchArtworks> {
        return repository.searchArtworkByName(query)
    }
}