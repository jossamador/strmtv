package com.example.strmtv.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.strmtv.presentation.home.HomeScreen
import com.example.strmtv.presentation.detail.DetailScreen
import com.example.strmtv.presentation.search.SearchScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val SEARCH = "search"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen()
        }
        composable(Routes.DETAIL) {
            DetailScreen()
        }
        composable(Routes.SEARCH) {
            SearchScreen()
        }
    }
}