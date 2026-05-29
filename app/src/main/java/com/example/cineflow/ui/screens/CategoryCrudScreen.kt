package com.example.cineflow.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
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
import com.example.cineflow.data.model.Category
import com.example.cineflow.ui.theme.*
import com.example.cineflow.ui.viewmodel.CineFlowViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCrudScreen(
    viewModel: CineFlowViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()
    val movies by viewModel.movies.collectAsState()
    val context = LocalContext.current

    var categorySearchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var editCategoryName by remember { mutableStateOf("") }

    val filteredCategories = remember(categories, categorySearchQuery) {
        if (categorySearchQuery.isBlank()) categories
        else categories.filter { it.name.contains(categorySearchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(CineDarkBackground)) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Gestionar Categorías",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
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
                    actions = {

                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CineDarkBackground,
                        titleContentColor = Color.White
                    )
                )

                // Barra de búsqueda que coincide con el mockup
                OutlinedTextField(
                    value = categorySearchQuery,
                    onValueChange = { categorySearchQuery = it },
                    placeholder = { Text("Filtrar categorías...", color = CineTextMuted, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.FilterList, contentDescription = null, tint = CineTextMuted, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = CineDarkSurface,
                        unfocusedContainerColor = CineDarkSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = CineRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(24.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Añadir Categoría", fontWeight = FontWeight.Bold) }
            )
        },
        containerColor = CineDarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 80.dp, top = 8.dp)
        ) {
            items(filteredCategories) { category ->
                val movieCount = movies.count { it.category?.id == category.id }
                
                CategoryItem(
                    category = category,
                    movieCount = movieCount,
                    onEditClick = {
                        categoryToEdit = category
                        editCategoryName = category.name
                    },
                    onDeleteClick = {
                        viewModel.deleteCategory(category.id) {
                            Toast.makeText(context, "Category deleted", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }

        // Add Category Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                containerColor = CineDarkSurface,
                title = { Text("Nueva Categoría", color = Color.White) },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Nombre de la categoría") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = CineGold,
                            unfocusedBorderColor = CineBorderColor
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addCategory(
                                name = newCategoryName,
                                onSuccess = {
                                    newCategoryName = ""
                                    showAddDialog = false
                                    Toast.makeText(context, "Categoría añadida", Toast.LENGTH_SHORT).show()
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CineRed)
                    ) {
                        Text("Añadir")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancelar", color = CineTextSecondary)
                    }
                }
            )
        }

        // Edit Category Dialog
        if (categoryToEdit != null) {
            AlertDialog(
                onDismissRequest = { categoryToEdit = null },
                containerColor = CineDarkSurface,
                title = { Text("Editar Categoría", color = Color.White) },
                text = {
                    OutlinedTextField(
                        value = editCategoryName,
                        onValueChange = { editCategoryName = it },
                        label = { Text("Nombre de la categoría") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = CineGold,
                            unfocusedBorderColor = CineBorderColor
                        )
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val updated = categoryToEdit!!.copy(name = editCategoryName)
                            viewModel.updateCategory(
                                category = updated,
                                onSuccess = {
                                    categoryToEdit = null
                                    Toast.makeText(context, "Categoría actualizada", Toast.LENGTH_SHORT).show()
                                },
                                onFailure = { error ->
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CineRed)
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { categoryToEdit = null }) {
                        Text("Cancelar", color = CineTextSecondary)
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    movieCount: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CineDarkSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Nombre y Contador
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = if (movieCount == 1) "$movieCount Película" else "$movieCount Películas",
                color = CineTextSecondary,
                fontSize = 12.sp
            )
        }

        // Acciones
        IconButton(onClick = onEditClick) {
            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = CineTextSecondary, modifier = Modifier.size(20.dp))
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = CineTextSecondary, modifier = Modifier.size(20.dp))
        }
    }
}
