package com.renatosanz.artxplorer.data.remote.api

import com.renatosanz.artxplorer.data.remote.dto.ArtworkDto
import com.google.gson.annotations.SerializedName

data class HomePageArtworks(
    val pagination: Pagination,
    val data: List<ArtworkDto>,
    val info: ExtraInfo,
    val config: ApiConfig
)


data class SearchArtworks(
    val pagination: Pagination,
    val data: List<SearchResult>,
    val info: ExtraInfo,
    val config: ApiConfig
)

data class SearchResult(
    @SerializedName("_score")
    val score: Double,
    val id: Long,
    @SerializedName("api_model")
    val apiModel: String?,
    @SerializedName("api_link")
    val apiLink: String?,
    val title: String,
    val thumbnail: Thumbnail?,
    val timestamp: String?,
    @SerializedName("is_boosted")
    val isBoosted: Boolean?,
    @SerializedName("image_id")
    val imageId: String?,
    @SerializedName("artist_title")
    val artistTitle: String?,
    @SerializedName("date_display")
    val dateDisplay: String?,
)

data class Thumbnail(
    val lqip: String,
    val width: Long,
    val height: Long,
    @SerializedName("alt_text")
    val altText: String,
)

data class Pagination(
    val total: Long,
    val limit: Number,
    val offset: Number,
    val total_pages: Int,
    val current_page: Int,
    val next_url: String,
)

data class ExtraInfo(
    val license_text: String,
    val license_links: List<String>,
    val version: String
)

data class ApiConfig(
    val iiif_url: String, val website_url: String,
)

data class SingleArtwork(
    val data: ArtworkDto,
    val info: ExtraInfo,
    val config: ApiConfig
)

data class ArtistArtworks(
    val pagination: Pagination,
    val data: List<RecommendationDto>
)

data class RecommendationDto(
    val id: Long,
    val title: String?,
    @SerializedName("image_id")
    val imageId: String?,
    @SerializedName("date_display")
    val dateDisplay: String?,
    @SerializedName("artist_title")
    val artistTitle: String?
)