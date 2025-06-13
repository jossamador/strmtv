package com.example.strmtv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.strmtv.presentation.home.SharedViewModel
import com.example.strmtv.presentation.home.navigation.NavGraph
import com.example.strmtv.ui.theme.StrmtvTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Aquí creas el SharedViewModel
        val sharedViewModel: SharedViewModel by viewModels()

        setContent {
            StrmtvTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF0F0F0F) // Fondo Apple TV+
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        sharedViewModel = sharedViewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StrmtvTheme {
        Text(
            text = "Hello Strmtv!",
            fontSize = 20.sp,
            color = Color.White // Fondo oscuro → texto blanco
        )
    }
}