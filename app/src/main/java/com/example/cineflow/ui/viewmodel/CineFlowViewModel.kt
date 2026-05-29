package com.example.cineflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cineflow.data.model.Category
import com.example.cineflow.data.model.Play
import com.example.cineflow.data.model.PlayWithCategory
import com.example.cineflow.data.repository.CineFlowRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CineFlowViewModel(private val repository: CineFlowRepository) : ViewModel() {

    val categories: StateFlow<List<Category>> = repository.getCategoriesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val movies: StateFlow<List<PlayWithCategory>> = repository.getMoviesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("ALL")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    // --- Reactive Filtered Movies ---
    val filteredMovies: StateFlow<List<PlayWithCategory>> = combine(
        movies,
        _searchQuery,
        _selectedCategoryFilter
    ) { movieList, query, filter ->
        movieList.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.play.title.contains(query, ignoreCase = true) ||
                    item.play.synopsis.contains(query, ignoreCase = true)

            val matchesFilter = filter == "ALL" ||
                    item.category?.name?.equals(filter, ignoreCase = true) == true

            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategoryFilter(filter: String) {
        _selectedCategoryFilter.value = filter
    }

    // --- CATEGORIES CRUD ---

    fun addCategory(name: String, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        if (name.isBlank()) {
            onFailure("Name cannot be empty")
            return
        }
        viewModelScope.launch {
            try {
                val id = repository.insertCategory(name)
                if (id > 0) {
                    onSuccess()
                } else {
                    onFailure("Category name must be unique")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Error adding category")
            }
        }
    }

    fun updateCategory(category: Category, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        if (category.name.isBlank()) {
            onFailure("Name cannot be empty")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateCategory(category)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: "Fail to update category")
            }
        }
    }

    fun deleteCategory(id: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteCategory(id)
            onSuccess()
        }
    }

    // --- MOVIES CRUD ---

    fun addMovie(play: Play, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        if (play.title.isBlank()) {
            onFailure("Title cannot be empty")
            return
        }
        viewModelScope.launch {
            try {
                val id = repository.insertMovie(play)
                if (id > 0) {
                    onSuccess()
                } else {
                    onFailure("Fail to add movie")
                }
            } catch (e: Exception) {
                onFailure(e.message ?: "Error adding movie")
            }
        }
    }

    fun updateMovie(play: Play, onSuccess: () -> Unit = {}, onFailure: (String) -> Unit = {}) {
        if (play.title.isBlank()) {
            onFailure("Title cannot be empty")
            return
        }
        viewModelScope.launch {
            try {
                repository.updateMovie(play)
                onSuccess()
            } catch (e: Exception) {
                onFailure(e.message ?: "Fail to update movie")
            }
        }
    }

    fun deleteMovie(id: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteMovie(id)
            onSuccess()
        }
    }

    suspend fun getMovieById(id: Long): PlayWithCategory? {
        return repository.getMovieById(id)
    }
}
