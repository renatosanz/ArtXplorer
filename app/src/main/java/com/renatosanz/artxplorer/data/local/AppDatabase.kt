package com.renatosanz.artxplorer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.renatosanz.artxplorer.data.local.dao.ArtworkDao
import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity

@Database(entities = [ArtworkEntity::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun artworkDao(): ArtworkDao
}