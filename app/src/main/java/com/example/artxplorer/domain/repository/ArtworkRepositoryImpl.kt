package com.example.artxplorer.domain.repository

import android.util.Log
import com.example.artxplorer.data.local.dao.ArtworkDao
import com.example.artxplorer.data.local.entities.ArtworkEntity
import com.example.artxplorer.data.remote.api.ApiService
import com.example.artxplorer.data.remote.api.HomePageArtworks
import com.example.artxplorer.data.remote.api.SearchArtworks
import com.example.artxplorer.data.remote.dto.toDomain
import com.example.artxplorer.data.repository.ArtworkRepository
import com.example.artxplorer.data.repository.GetArtworkResult
import com.example.artxplorer.domain.model.Artwork
import kotlin.random.Random

// data/repository/UserRepositoryImpl.kt
class ArtworkRepositoryImpl(
    private val apiService: ApiService, private val artworkDao: ArtworkDao
) : ArtworkRepository {

    override suspend fun saveArtwork(artwork: ArtworkEntity): Result<Boolean> {
        return try {
            Log.d(
                "ArtworkRepositoryImpl",
                "saving artwork ArtworkRepositoryImpl.saveArtwork: $artwork"
            )
            artworkDao.insertAll(artwork)
            Log.d("ArtworkRepositoryImpl", "result : $artwork")
            Result.success(true)
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "error: $e")
            Result.failure(e)
        }
    }

    override suspend fun deleteArtwork(artwork: ArtworkEntity) {
        try {
            Log.d(
                "ArtworkRepositoryImpl",
                "deleting artwork ArtworkRepositoryImpl.deleteArtwork: $artwork"
            )
            artworkDao.delete(artwork)
            Log.d("ArtworkRepositoryImpl", "result : $artwork")
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "error: $e")
        }
    }

    override suspend fun clearSavedArtworks(): Result<Boolean> {
        return try {
            artworkDao.deleteAll()
            Result.success(true)
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "error: $e")
            Result.failure(e)
        }
    }

    override suspend fun getArtwork(id: Long): Result<GetArtworkResult> {
        return try {
            Log.d(
                "ArtworkRepositoryImpl",
                "retrieving artwork on ArtworkRepositoryImpl.getArtwork: $id"
            )

            val dataFromDB = artworkDao.getById(id);
            if (dataFromDB != null) {
                Log.d(
                    "ArtworkRepositoryImpl", "Artwork found on db! (id: ${dataFromDB.id})"
                )
                return Result.success(GetArtworkResult.FoundOnLocal(dataFromDB))
            }

            val artwork = apiService.getArtwork(id).data.toDomain()
            Log.d("ArtworkRepositoryImpl", "result : $artwork")

            Result.success(GetArtworkResult.FoundOnApi(artwork))
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "error: $e")
            Result.failure(e)
        }
    }

    override suspend fun getRandomArtwork(randomMax: Long): Result<Artwork> {
        val rand = Random(System.currentTimeMillis())
        Log.d(this.javaClass.toString(), "getting random image")
        repeat(10) {
            try {
                val artworkId = rand.nextLong() % randomMax
                Log.d(this.javaClass.toString(), "Trying id:$artworkId ...")
                return Result.success(apiService.getArtwork(artworkId).data.toDomain())
            } catch (e: Exception) {
                Log.d("ArtworkRepositoryImpl", "trying again...")
            }
        }
        return Result.failure(Exception("Failed after 3 attempts"))
    }

    override suspend fun getHomePage(page: Int): Result<HomePageArtworks> {
        return try {
            val data = apiService.getHomePage(page)
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecommendations(
        artistId: Long,
        excludeArtworkId: Long
    ): Result<List<Artwork>> {
        return try {
            // Two tiny requests: page 1 carries the total and doubles as candidate pool,
            // the random page adds variety. Both are capped at 4 items, fields trimmed.
            val firstPage = apiService.getArtworksByArtist(artistId, page = 1)
            val pageCount = ((firstPage.pagination.total + 3) / 4).toInt()
            val randomPage = if (pageCount > 1) {
                apiService.getArtworksByArtist(
                    artistId,
                    page = Random.nextInt(2, pageCount + 1)
                )
            } else null

            val picks = (firstPage.data + randomPage?.data.orEmpty())
                .distinctBy { it.id }
                .filter { it.id != excludeArtworkId }
                .shuffled()
                .take(3)
                .map {
                    Artwork(
                        id = it.id,
                        title = it.title ?: "No title",
                        artist = it.artistTitle.orEmpty(),
                        imageId = it.imageId,
                        dateDisplay = it.dateDisplay
                    )
                }
            Result.success(picks)
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "recommendations error: $e")
            Result.failure(e)
        }
    }

    override suspend fun getSavedArtwoks(): Result<List<Artwork>> {
        return try {
            val data = artworkDao.getAll()
            Result.success(data.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchArtworkByName(query: String): Result<SearchArtworks> {
        return try {
            Log.d(
                "ArtworkRepositoryImpl",
                "search for query: $query"
            )

            val response = apiService.searchArtworks(query)
            Log.d("ArtworkRepositoryImpl", " search result : $response")

            Result.success(response)
        } catch (e: Exception) {
            Log.d("ArtworkRepositoryImpl", "error: $e")
            Result.failure(e)
        }
    }
}