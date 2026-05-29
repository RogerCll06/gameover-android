package com.example.cineflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflow.data.model.PlayWithCategory
import com.example.cineflow.ui.theme.*

/**
 * Returns a gorgeous, tailored gradient palette based on the poster key/genre.
 */
fun getMovieGradient(posterKey: String): List<Color> {
    return when (posterKey.lowercase().trim()) {
        "cyberpunk", "sci-fi" -> listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364), Color(0xFFE50914))
        "cosmic" -> listOf(Color(0xFF1F1C2C), Color(0xFF928DAB), Color(0xFFF5C518))
        "racer", "action" -> listOf(Color(0xFF111111), Color(0xFFE50914), Color(0xFFF5C518))
        "horror" -> listOf(Color(0xFF0F0C20), Color(0xFF1E1015), Color(0xFF3A0007))
        "desert", "drama" -> listOf(Color(0xFF3E5151), Color(0xFFDECBA4), Color(0xFFF5C518))
        "comedy" -> listOf(Color(0xFF1A2A6C), Color(0xFFB21F1F), Color(0xFFFDBB2D))
        else -> {
            // Hash the string to generate a unique gradient!
            val hash = posterKey.hashCode()
            val c1 = Color(0xFF000000L or (hash.toLong() and 0x00FFFFFFL))
            val c2 = Color(0xFF000000L or (hash.shr(8).toLong() and 0x00FFFFFFL))
            val c3 = CineGold
            listOf(c1, c2, c3)
        }
    }
}

/**
 * A beautiful, procedural movie poster component.
 * Fallbacks to a vibrant dynamic gradient if no valid image URL is present.
 */
@Composable
fun MoviePoster(
    posterPath: String,
    title: String,
    modifier: Modifier = Modifier,
    showOverlayPlay: Boolean = false
) {
    val isUri = posterPath.startsWith("content://") || posterPath.startsWith("file://")
    val gradientColors = getMovieGradient(posterPath.ifBlank { title })

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(gradientColors))
            .border(1.dp, CineCardBorderColor, RoundedCornerShape(16.dp))
    ) {
        if (isUri) {
            Image(
                painter = rememberAsyncImagePainter(posterPath),
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Overlay film texture design
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Placeholder for any other design element if needed
        }

        // Poster details (film logo & typography helper)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Middle play icon overlay (optional)
            if (showOverlayPlay) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(CineGold, CircleShape)
                        .shadow(8.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = CineDarkBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Bottom Movie Title display in poster
            Text(
                text = title.uppercase(),
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Interactive filter chip matching our UI mockup.
 */
@Composable
fun CategoryFilterChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) CineGold else CineDarkSurfaceVariant)
            .border(
                width = 1.dp,
                color = if (isSelected) CineGoldVariant else CineBorderColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.uppercase(),
            color = if (isSelected) CineDarkBackground else Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Grid item displaying a movie card in the catalog.
 */
@Composable
fun MovieGridCard(
    movieWithCategory: PlayWithCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val movie = movieWithCategory.play
    val categoryName = movieWithCategory.category?.name ?: "General"

    Column(
        modifier = modifier
            .width(160.dp)
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.7f)
        ) {
            MoviePoster(
                posterPath = movie.posterPath,
                title = movie.title,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Movie Title
        Text(
            text = movie.title,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * A beautiful translucent box overlay providing that glassy premium aesthetic.
 */
@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(CineDarkSurface.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
            .border(1.dp, CineCardBorderColor, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        content()
    }
}

/**
 * Cast Member Circular Profile Avatar
 */
@Composable
fun CastMemberAvatar(
    name: String,
    role: String,
    modifier: Modifier = Modifier
) {
    val initials = name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2)
        .joinToString("")
        .uppercase()

    val backgroundGradients = listOf(
        listOf(Color(0xFF2C3E50), Color(0xFF3498DB)),
        listOf(Color(0xFF16A085), Color(0xFF1ABC9C)),
        listOf(Color(0xFFD35400), Color(0xFFE67E22)),
        listOf(Color(0xFF8E44AD), Color(0xFF9B59B6)),
        listOf(Color(0xFF27AE60), Color(0xFF2ECC71))
    )
    val hashIndex = Math.abs(name.hashCode() % backgroundGradients.size)
    val gradient = backgroundGradients[hashIndex]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Brush.verticalGradient(gradient))
                .border(2.dp, CineGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = role,
            color = CineTextMuted,
            fontSize = 8.sp,
            maxLines = 1,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}
