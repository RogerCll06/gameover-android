package com.example.cineflow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflow.data.model.PlayWithCategory
import com.example.cineflow.ui.components.MoviePoster
import com.example.cineflow.ui.theme.*
import com.example.cineflow.ui.viewmodel.CineFlowViewModel

@Composable
fun MovieDetailScreen(
    viewModel: CineFlowViewModel,
    movieId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var movieWithCat by remember { mutableStateOf<PlayWithCategory?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Fetch movie details on load
    LaunchedEffect(movieId) {
        movieWithCat = viewModel.getMovieById(movieId)
    }

    if (movieWithCat == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CineDarkBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = CineGold)
        }
        return
    }

    val item = movieWithCat!!
    val movie = item.play
    val categoryName = item.category?.name ?: "General"

    Scaffold(
        containerColor = CineDarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // --- SECTION 1: HERO IMAGE / BACKDROP ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Procedural Poster Card (larger representation)
                MoviePoster(
                    posterPath = movie.posterPath,
                    title = "",
                    showOverlayPlay = false,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlaid buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable { onBackClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // --- SECTION 2: MOVIE GENERAL INFORMATION ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = movie.title.uppercase(),
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    lineHeight = 32.sp
                )

                // Genres chips (Horizontal)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = categoryName.uppercase(),
                            color = CineGold,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                HorizontalDivider(color = CineBorderColor)

                // --- SECTION 3: SYNOPSIS ---
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Sinopsis",
                        color = CineGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = movie.synopsis.ifBlank { "No hay un resumen disponible para esta película." },
                        color = CineTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // --- SECTION 6: ACTION BUTTONS (EDIT & DELETE) ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Edit
                    OutlinedButton(
                        onClick = { onEditClick(movie.id) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CineGold
                        ),
                        border = BorderStroke(1.dp, CineGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Editar", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Delete
                    Button(
                        onClick = { showDeleteConfirm = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CineRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                modifier = Modifier.size(16.dp)
                            )
                            Text("Eliminar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- DIALOG: DELETE CONFIRMATION ---
        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                containerColor = CineDarkSurface,
                title = {
                    Text(
                        text = "¿ELIMINAR JUEGO?",
                        color = CineRed,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que quieres eliminar '${movie.title}'? Esta acción no se puede deshacer.",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteMovie(
                                id = movie.id,
                                onSuccess = {
                                    showDeleteConfirm = false
                                    Toast.makeText(context, "¡Juego eliminada!", Toast.LENGTH_SHORT).show()
                                    onBackClick()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CineRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("ELIMINAR", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteConfirm = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = CineTextSecondary)
                    ) {
                        Text("CANCELAR")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(1.dp, CineCardBorderColor, RoundedCornerShape(16.dp))
            )
        }
    }
}
