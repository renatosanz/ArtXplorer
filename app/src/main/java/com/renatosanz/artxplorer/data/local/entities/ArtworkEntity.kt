package com.renatosanz.artxplorer.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.renatosanz.artxplorer.domain.model.Artwork

@Entity("artwork")
data class ArtworkEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "image_id") val imageId: String?,
    @ColumnInfo(name = "place_of_origin") val placeOfOrigin: String?,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "dimentions") val dimentions: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "medium_display") val mediumDisplay: String?,
    @ColumnInfo(name = "date_start") val dateStart: Int?,
    @ColumnInfo(name = "date_end") val dateEnd: Int?,
    @ColumnInfo(name = "date_display") val dateDisplay: String?,
    @ColumnInfo(name = "artist_id") val artistId: Long?,
    @ColumnInfo(name = "credit_line") val creditLine: String?,
    @ColumnInfo(name = "copyright_notice") val copyrightNotice: String?,
    @ColumnInfo(name = "style_title") val styleTitle: String?,
    @ColumnInfo(name = "classification_titles") val classificationTitles: String?,
    @ColumnInfo(name = "local_image_path ") val localImagePath: String?
) {
    fun toDomain(): Artwork {
        return Artwork(
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
            classificationTitles = classificationTitles
        )
    }
}
