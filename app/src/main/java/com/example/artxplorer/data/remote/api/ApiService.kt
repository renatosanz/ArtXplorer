package com.example.artxplorer.data.remote.api

import com.example.artxplorer.data.remote.dto.ArtworkDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Only the fields the detail screen renders: the default payload also ships
// publication/exhibition history, provenance and suggestions.
private const val ARTWORK_DETAIL_FIELDS =
    "id,title,artist_display,artist_id,place_of_origin,dimensions,medium_display," +
        "description,date_display,image_id,credit_line,copyright_notice,style_title," +
        "classification_titles"

private const val RECOMMENDATION_FIELDS = "id,title,image_id,date_display,artist_title"

// image_id is not part of the default search payload; thumbnail keeps the
// alt text used by the result dialog.
private const val SEARCH_RESULT_FIELDS =
    "id,title,image_id,artist_title,date_display,thumbnail"

interface ApiService {
    @GET("artworks")
    suspend fun getHomePage(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 18
    ): HomePageArtworks

    @GET("artworks/{artworkId}")
    suspend fun getArtwork(
        @Path("artworkId") artworkId: Long,
        @Query("fields") fields: String = ARTWORK_DETAIL_FIELDS
    ): SingleArtwork

    @GET("artworks/search")
    suspend fun getArtworksByArtist(
        @Query("query[term][artist_id]") artistId: Long,
        @Query("fields") fields: String = RECOMMENDATION_FIELDS,
        @Query("limit") limit: Int = 4,
        @Query("page") page: Int = 1
    ): ArtistArtworks

    @GET(value = "artworks/search")
    suspend fun searchArtworks(
        @Query(value = "q") queryString: String,
        @Query("fields") fields: String = SEARCH_RESULT_FIELDS
    ): SearchArtworks
}
