package com.example.artxplorer.data.repository

import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.data.remote.api.HomePageArtworks
import com.example.artxplorer.data.remote.api.SearchArtworks
import com.example.artxplorer.domain.model.Artwork

sealed class GetArtworkResult {
    data class FoundOnApi(val artwork: Artwork) : GetArtworkResult()
    data class FoundOnLocal(val artworkEntity: ArtworkEntity) : GetArtworkResult()
    data class Error(val exception: Exception) : GetArtworkResult()
}


interface ArtworkRepository {
    suspend fun saveArtwork(artwork: ArtworkEntity): Result<Boolean>
    suspend fun deleteArtwork(artwork: ArtworkEntity)
    suspend fun clearSavedArtworks(): Result<Boolean>

    suspend fun getArtwork(id: Long): Result<GetArtworkResult>
    suspend fun getRandomArtwork(randomMax: Long): Result<Artwork>
    suspend fun getHomePage(page: Int): Result<HomePageArtworks>

    suspend fun getSavedArtwoks(): Result<List<Artwork>>
    suspend fun searchArtworkByName(query: String): Result<SearchArtworks>
}