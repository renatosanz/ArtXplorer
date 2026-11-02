package com.renatosanz.artxplorer.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.renatosanz.artxplorer.data.local.entities.ArtworkEntity
import com.renatosanz.artxplorer.domain.model.Artwork

@Dao
interface ArtworkDao {
    @Query("SELECT * FROM artwork")
    suspend fun getAll(): List<ArtworkEntity>

    @Query("SELECT * FROM artwork WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<ArtworkEntity>

    @Query("SELECT * FROM artwork WHERE id = :id")
    fun getById(id: Long): ArtworkEntity?

    @Query(
        "SELECT * FROM artwork WHERE title LIKE :title LIMIT 1"
    )
    fun findByName(title: String): ArtworkEntity

    @Insert
    fun insertAll(vararg users: ArtworkEntity)

    @Update
    fun update(artwork: ArtworkEntity)

    @Delete
    fun delete(artwork: ArtworkEntity)

    @Query("DELETE FROM artwork")
    suspend fun deleteAll()
}