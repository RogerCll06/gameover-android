package com.example.cineflow.data.local

import androidx.room.*
import com.example.cineflow.data.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category): Int

    @Delete
    suspend fun deleteCategory(category: Category): Int

    @Query("SELECT COUNT(*) FROM categories")
    fun getCategoryCount(): Flow<Int>
}
