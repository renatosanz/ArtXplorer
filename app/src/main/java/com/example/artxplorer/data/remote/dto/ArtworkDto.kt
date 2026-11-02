package com.example.artxplorer.data.remote.dto

import com.example.artxplorer.domain.model.Artwork

data class ArtworkDto(
    val id: Long,
    val place_of_origin: String?,
    val image_id: String?,
    val title: String?,
    val artist_display: String?,
    val dimensions: String?,
    val description: String?,
    val medium_display: String?,
    val date_start: Int?,
    val date_end: Int?,
    val date_display: String?,
    val artist_id: Long?,
    val credit_line: String?,
    val copyright_notice: String?,
    val style_title: String?,
    val classification_titles: List<String>?
)

fun ArtworkDto.toDomain() = Artwork(
    id = id,
    imageId = image_id ?: "noimage",
    title = title ?: "No title",
    artist = artist_display ?: "No Artist",
    placeOfOrigin = place_of_origin ?: "Place unknown",
    dimentions = dimensions ?: "Dimentions unknown",
    description = description ?: "No description",
    mediumDisplay = medium_display ?: "Medium unknown",
    dateStart = date_start ?: 0,
    dateEnd = date_end ?: 0,
    dateDisplay = date_display ?: "Date unknown",
    artistId = artist_id,
    creditLine = credit_line,
    copyrightNotice = copyright_notice,
    styleTitle = style_title,
    classificationTitles = classification_titles?.joinToString(", ")
)
