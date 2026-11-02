package com.example.artxplorer.domain.usecase

import com.example.artxplorer.data.remote.api.HomePageArtworks
import com.example.artxplorer.data.repository.ArtworkRepository

class GetHomePageUseCase (
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(page:Int = 1): Result<HomePageArtworks> {
        return repository.getHomePage(page)
    }
}
