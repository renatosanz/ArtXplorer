package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.remote.api.SearchArtworks
import com.renatosanz.artxplorer.data.repository.ArtworkRepository

class SearchArtworkUseCase(
    private val repository: ArtworkRepository
) {
    suspend operator fun invoke(query:String): Result<SearchArtworks> {
        return repository.searchArtworkByName(query)
    }
}