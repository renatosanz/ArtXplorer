package com.renatosanz.artxplorer.domain.model

import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity


data class Artwork(
    val id: Long = 0,
    val title: String = "Unknown Title",
    val artist: String = "Unknown Artist",
    val imageId: String? = null,
    val placeOfOrigin: String? = null,
    val dimentions: String? = null,
    val description: String? = null,
    val mediumDisplay: String? = null,
    val dateStart: Int? = null,
    val dateEnd: Int? = null,
    val dateDisplay: String? = null,
    val artistId: Long? = null,
    val creditLine: String? = null,
    val copyrightNotice: String? = null,
    val styleTitle: String? = null,
    val classificationTitles: String? = null
) {
    fun toEntity(): ArtworkEntity {
        return ArtworkEntity(
            id = id,
            imageId = imageId,
            placeOfOrigin = placeOfOrigin,
            title = title,
            artist = artist,
            dimentions = dimentions,
            description = description,
            mediumDisplay = mediumDisplay,
            dateStart = dateStart,
            dateEnd = dateEnd,
            dateDisplay = dateDisplay,
            artistId = artistId,
            creditLine = creditLine,
            copyrightNotice = copyrightNotice,
            styleTitle = styleTitle,
            classificationTitles = classificationTitles,
            localImagePath = null
        )
    }
}
