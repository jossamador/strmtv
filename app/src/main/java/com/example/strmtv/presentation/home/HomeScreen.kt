package com.example.strmtv.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.strmtv.presentation.home.navigation.Routes

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Pantalla de Inicio", modifier = Modifier.padding(bottom = 16.dp))

        Button(onClick = { navController.navigate(Routes.SEARCH) }) {
            Text("Ir a Buscar en TMDB")
        }
    }
}