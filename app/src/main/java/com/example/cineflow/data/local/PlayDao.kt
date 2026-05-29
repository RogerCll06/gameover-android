package com.example.cineflow.data.local

import androidx.room.*
import com.example.cineflow.data.model.Play
import com.example.cineflow.data.model.PlayWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayDao {
    @Transaction
    @Query("SELECT * FROM movies ORDER BY id DESC")
    fun getAllMovies(): Flow<List<PlayWithCategory>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Long): PlayWithCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(play: Play): Long

    @Update
    suspend fun updateMovie(play: Play): Int

    @Delete
    suspend fun deleteMovie(play: Play): Int
}
