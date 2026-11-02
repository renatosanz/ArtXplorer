package com.example.artxplorer.domain.model

import com.example.artxplorer.data.local.entities.ArtworkEntity


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
    val dateDisplay: String? = null
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
            localImagePath = null
        )
    }
}