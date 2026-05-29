package com.example.cineflow.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cineflow.R
import com.example.cineflow.ui.theme.CineGold
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier
) {

    var startAnimation by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "BackgroundScale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(7000)
        onTimeout()
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )

        // Oscurecer imagen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
        )

        // Contenido encima
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            Text(
                text = "GameOver",
                color = CineGold,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Todo lo que buscas está aquí",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}