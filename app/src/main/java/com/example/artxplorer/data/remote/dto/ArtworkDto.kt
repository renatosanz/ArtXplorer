package com.example.artxplorer.data.remote.dto

import com.example.artxplorer.domain.model.Artwork

data class ArtworkDto(
    val id: Long,
    val place_of_origin: String?,
    val image_id: String?,
    val title: String?,
    val artist_display: String?,
    val dimentions: String?,
    val description: String?,
    val medium_display: String?,
    val date_start: Int?,
    val date_end: Int?,
    val date_display: String?
)

fun ArtworkDto.toDomain() = Artwork(
    id = id,
    imageId = image_id ?: "noimage",
    title = title ?: "No title",
    artist = artist_display ?: "No Artist",
    placeOfOrigin = place_of_origin ?: "Place unknown",
    dimentions = dimentions ?: "Dimentions unknown",
    description = description ?: "No description",
    mediumDisplay = medium_display ?: "Medium unknown",
    dateStart = date_start ?: 0,
    dateEnd = date_end ?: 0,
    dateDisplay = date_display ?: "Date unknown"
)