package com.example.cineflow.data.repository

import com.example.cineflow.data.local.AppDatabase
import com.example.cineflow.data.model.Category
import com.example.cineflow.data.model.Play
import com.example.cineflow.data.model.PlayWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class CineFlowRepository(private val database: AppDatabase) {

    private val movieDao = database.movieDao()
    private val categoryDao = database.categoryDao()

    // --- CATEGORIES CRUD ---

    fun getCategoriesFlow(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun getCategories(): List<Category> = withContext(Dispatchers.IO) {
        categoryDao.getAllCategories().first()
    }

    suspend fun insertCategory(name: String): Long = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(Category(name = name.trim()))
    }

    suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.updateCategory(category)
    }

    suspend fun deleteCategory(id: Long) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategory(Category(id = id, name = ""))
    }

    // --- MOVIES CRUD ---

    fun getMoviesFlow(): Flow<List<PlayWithCategory>> = movieDao.getAllMovies()

    suspend fun getMovies(): List<PlayWithCategory> = withContext(Dispatchers.IO) {
        movieDao.getAllMovies().first()
    }

    suspend fun getMovieById(id: Long): PlayWithCategory? = withContext(Dispatchers.IO) {
        movieDao.getMovieById(id)
    }

    suspend fun insertMovie(play: Play): Long = withContext(Dispatchers.IO) {
        movieDao.insertMovie(play)
    }

    suspend fun updateMovie(play: Play) = withContext(Dispatchers.IO) {
        movieDao.updateMovie(play)
    }

    suspend fun deleteMovie(id: Long) = withContext(Dispatchers.IO) {
        movieDao.deleteMovie(Play(id = id, title = "", synopsis = "", posterPath = "", categoryId = null))
    }
}
