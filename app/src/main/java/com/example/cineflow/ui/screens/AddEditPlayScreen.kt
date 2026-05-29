package com.example.cineflow.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.cineflow.data.model.Play
import com.example.cineflow.ui.components.MoviePoster
import com.example.cineflow.ui.theme.*
import com.example.cineflow.ui.viewmodel.CineFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMovieScreen(
    viewModel: CineFlowViewModel,
    movieId: Long?,
    onBackClick: () -> Unit,
    onNavigateToCategories: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val isEditMode = movieId != null


    var title by remember { mutableStateOf("") }
    var synopsis by remember { mutableStateOf("") }
    var posterStyle by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                val contentResolver = context.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION
                contentResolver.takePersistableUriPermission(it, takeFlags)
            } catch (e: Exception) {
            }
            posterStyle = it.toString()
        }
    }

    LaunchedEffect(movieId) {
        if (isEditMode && movieId != null) {
            val movieWithCat = viewModel.getMovieById(movieId)
            movieWithCat?.let {
                title = it.play.title
                synopsis = it.play.synopsis
                posterStyle = it.play.posterPath
                selectedCategoryId = it.play.categoryId
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isEditMode) "EDITAR JUEGO" else "AÑADIR JUEGO",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CineDarkBackground,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = CineDarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "PÓSTER DEL JUEGO",
                color = CineTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CineDarkSurface)
                    .border(1.dp, CineBorderColor, RoundedCornerShape(16.dp))
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (posterStyle.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = CineTextMuted,
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "TOCA PARA SUBIR IMAGEN",
                            color = CineTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    MoviePoster(
                        posterPath = posterStyle,
                        title = title,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Cambiar imagen",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "TÍTULO DEL JUEGO",
                    color = CineTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("ej. Resin Evil", color = CineTextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CineGold,
                        unfocusedBorderColor = CineBorderColor,
                        focusedContainerColor = CineDarkSurface,
                        unfocusedContainerColor = CineDarkSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CATEGORÍA",
                        color = CineTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Selecciona una categoría",
                        color = CineTextMuted,
                        fontSize = 9.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategoryId == category.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) CineGold else CineDarkSurface)
                                .border(1.dp, if (isSelected) CineGoldVariant else CineBorderColor, RoundedCornerShape(16.dp))
                                .clickable {
                                    selectedCategoryId = if (isSelected) null else category.id
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = category.name,
                                color = if (isSelected) CineDarkBackground else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(CineDarkSurfaceVariant)
                            .border(1.dp, CineBorderColor, CircleShape)
                            .clickable { onNavigateToCategories() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Manage Genres",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "SINOPSIS",
                    color = CineTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    placeholder = { Text("Describe brevemente el Juego ", color = CineTextMuted) },
                    minLines = 4,
                    maxLines = 8,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = CineGold,
                        unfocusedBorderColor = CineBorderColor,
                        focusedContainerColor = CineDarkSurface,
                        unfocusedContainerColor = CineDarkSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CineDarkSurfaceVariant,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(1.dp, CineBorderColor, RoundedCornerShape(12.dp))
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }

                // Save
                Button(
                    onClick = {
                        val play = Play(
                            id = movieId ?: 0L,
                            title = title,
                            synopsis = synopsis,
                            posterPath = posterStyle,
                            categoryId = selectedCategoryId
                        )

                        val successMsg = if (isEditMode) "¡Película actualizada!" else "¡Película añadida!"
                        val failureMsg = if (isEditMode) "Error al actualizar" else "Error al añadir"

                        if (isEditMode) {
                            viewModel.updateMovie(
                                play = play,
                                onSuccess = {
                                    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                                    onBackClick()
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            viewModel.addMovie(
                                play = play,
                                onSuccess = {
                                    Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
                                    onBackClick()
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CineRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text(
                        text = if (isEditMode) "Guardar " else "Guardar ",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
