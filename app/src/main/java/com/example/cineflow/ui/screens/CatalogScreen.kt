package com.example.cineflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflow.R
import com.example.cineflow.ui.components.CategoryFilterChip
import com.example.cineflow.ui.components.MovieGridCard
import com.example.cineflow.ui.components.MoviePoster
import com.example.cineflow.ui.theme.*
import com.example.cineflow.ui.viewmodel.CineFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    viewModel: CineFlowViewModel,
    onMovieClick: (Long) -> Unit,
    onAddMovieClick: () -> Unit,
    onManageCategoriesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val filteredMovies by viewModel.filteredMovies.collectAsState()
    val rawMovies by viewModel.movies.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedCategoryFilter.collectAsState()

    // Determine the "Featured Now" movie (the latest movie added)
    val featuredMovie = remember(rawMovies) {
        rawMovies.firstOrNull()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "GAME OVER",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            letterSpacing = 2.sp,
                            color = CineGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CineDarkBackground)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = CineDarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier.border(0.5.dp, CineBorderColor, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { viewModel.setSelectedCategoryFilter("ALL") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CineDarkBackground,
                        selectedTextColor = CineGold,
                        indicatorColor = CineGold,
                        unselectedIconColor = CineTextSecondary,
                        unselectedTextColor = CineTextMuted
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onManageCategoriesClick,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Categorías") },
                    label = { Text("Categorías") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = CineDarkBackground,
                        selectedTextColor = CineGold,
                        indicatorColor = CineGold,
                        unselectedIconColor = CineTextSecondary,
                        unselectedTextColor = CineTextMuted
                    )
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddMovieClick,
                containerColor = CineRed,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir juego",
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Añadir Juego",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        },
        containerColor = CineDarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Buscar juego",
                            color = CineTextMuted,
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = CineTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CineGold,
                        unfocusedBorderColor = CineBorderColor,
                        focusedContainerColor = CineDarkSurface,
                        unfocusedContainerColor = CineDarkSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp)
                )
            }

            item(span = { GridItemSpan(2) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    item {
                        CategoryFilterChip(
                            name = "ALL",
                            isSelected = selectedFilter == "ALL",
                            onClick = { viewModel.setSelectedCategoryFilter("ALL") }
                        )
                    }

                    items(categories) { category ->
                        CategoryFilterChip(
                            name = category.name,
                            isSelected = selectedFilter.equals(category.name, ignoreCase = true),
                            onClick = { viewModel.setSelectedCategoryFilter(category.name) }
                        )
                    }
                }
            }

            if (selectedFilter == "ALL" && searchQuery.isBlank() && featuredMovie != null) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DESTACADO AHORA",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, CineCardBorderColor, RoundedCornerShape(16.dp))
                                .clickable { onMovieClick(featuredMovie.play.id) }
                        ) {
                            MoviePoster(
                                posterPath = featuredMovie.play.posterPath,
                                title = "",
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.3f),
                                                Color.Black.copy(alpha = 0.85f)
                                            )
                                        )
                                    )
                                    .padding(16.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        featuredMovie.category?.name?.let { catName ->
                                            Box(
                                                modifier = Modifier
                                                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = catName.uppercase(),
                                                    color = Color.White,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }

                                    // Title
                                    Text(
                                        text = featuredMovie.play.title,
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Text(
                    text = if (searchQuery.isNotBlank() || selectedFilter != "ALL") "Resultados de búsqueda" else "Todos los juegos",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
            }

            if (filteredMovies.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron .",
                            color = CineTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredMovies) { movieWithCat ->
                    MovieGridCard(
                        movieWithCategory = movieWithCat,
                        onClick = { onMovieClick(movieWithCat.play.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}
