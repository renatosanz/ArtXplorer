package com.renatosanz.artxplorer.domain.usecase

import com.renatosanz.artxplorer.data.remote.api.HomePageArtworks
import com.renatosanz.artxplorer.data.repository.ArtworkRepository

class GetHomePageUseCase (
    private val repository: ArtworkRepository
){
    suspend operator fun invoke(page:Int = 1): Result<HomePageArtworks> {
        return repository.getHomePage(page)
    }
}
