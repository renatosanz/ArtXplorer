package com.example.artxplorer.data.remote.api

import com.example.artxplorer.data.remote.dto.ArtworkDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("artworks")
    suspend fun getHomePage(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 18
    ): HomePageArtworks

    @GET("artworks/{artworkId}")
    suspend fun getArtwork(@Path("artworkId") artworkId: Long): SingleArtwork

    @GET(value = "artworks/search")
    suspend fun searchArtworks(@Query(value = "q") queryString: String): SearchArtworks
}