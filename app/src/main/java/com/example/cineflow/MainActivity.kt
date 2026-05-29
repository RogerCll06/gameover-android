package com.example.cineflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cineflow.data.local.AppDatabase
import com.example.cineflow.data.repository.CineFlowRepository
import com.example.cineflow.ui.screens.AddEditMovieScreen
import com.example.cineflow.ui.screens.CatalogScreen
import com.example.cineflow.ui.screens.CategoryCrudScreen
import com.example.cineflow.ui.screens.MovieDetailScreen
import com.example.cineflow.ui.screens.WelcomeScreen
import com.example.cineflow.ui.theme.CineFlowTheme
import com.example.cineflow.ui.viewmodel.CineFlowViewModel

// --- STATE-BASED ROUTER SCREENS ---
sealed class Screen {
    object Welcome : Screen()
    object Catalog : Screen()
    data class MovieDetail(val movieId: Long) : Screen()
    data class AddEditMovie(val movieId: Long? = null) : Screen()
    data class ManageCategories(val returnToMovieId: Long? = null, val isComingFromAdd: Boolean = false) : Screen()
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize Room Database and Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CineFlowRepository(database)

        // 2. Instantiate ViewModel using standard Android Factory
        val viewModelFactory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CineFlowViewModel(repository) as T
            }
        }
        val viewModel: CineFlowViewModel by viewModels { viewModelFactory }

        setContent {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }

            CineFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (val screen = currentScreen) {
                        is Screen.Welcome -> {
                            WelcomeScreen(
                                onTimeout = { currentScreen = Screen.Catalog }
                            )
                        }

                        is Screen.Catalog -> {
                            CatalogScreen(
                                viewModel = viewModel,
                                onMovieClick = { id -> currentScreen = Screen.MovieDetail(id) },
                                onAddMovieClick = { currentScreen = Screen.AddEditMovie(null) },
                                onManageCategoriesClick = { currentScreen = Screen.ManageCategories() }
                            )
                        }

                        is Screen.MovieDetail -> {
                            BackHandler {
                                currentScreen = Screen.Catalog
                            }

                            MovieDetailScreen(
                                viewModel = viewModel,
                                movieId = screen.movieId,
                                onBackClick = { currentScreen = Screen.Catalog },
                                onEditClick = { id -> currentScreen = Screen.AddEditMovie(id) }
                            )
                        }

                        is Screen.AddEditMovie -> {
                            // Intercept physical Back button / gesture
                            BackHandler {
                                currentScreen = Screen.Catalog
                            }

                            AddEditMovieScreen(
                                viewModel = viewModel,
                                movieId = screen.movieId,
                                onBackClick = { currentScreen = Screen.Catalog },
                                onNavigateToCategories = { 
                                    currentScreen = Screen.ManageCategories(
                                        returnToMovieId = screen.movieId,
                                        isComingFromAdd = true
                                    ) 
                                }
                            )
                        }

                        is Screen.ManageCategories -> {
                            // Intercept physical Back button / gesture
                            BackHandler {
                                if (screen.isComingFromAdd) {
                                    currentScreen = Screen.AddEditMovie(screen.returnToMovieId)
                                } else {
                                    currentScreen = Screen.Catalog
                                }
                            }

                            CategoryCrudScreen(
                                viewModel = viewModel,
                                onBackClick = { 
                                    if (screen.isComingFromAdd) {
                                        currentScreen = Screen.AddEditMovie(screen.returnToMovieId)
                                    } else {
                                        currentScreen = Screen.Catalog
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}