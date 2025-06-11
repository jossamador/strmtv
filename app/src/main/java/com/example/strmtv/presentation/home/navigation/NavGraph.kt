package com.example.strmtv.presentation.home.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.strmtv.presentation.home.HomeScreen
import com.example.strmtv.presentation.home.detail.DetailScreen
import com.example.strmtv.presentation.home.search.SearchScreen
import android.net.Uri

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val SEARCH = "search"
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        composable(
            route = "${Routes.DETAIL}/{title}/{poster}/{releaseDate}/{description}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("poster") { type = NavType.StringType },
                navArgument("releaseDate") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            DetailScreen(
                navController = navController,
                poster = backStackEntry.arguments?.getString("poster"),
                title = backStackEntry.arguments?.getString("title"),
                releaseDate = backStackEntry.arguments?.getString("releaseDate"),
                description = backStackEntry.arguments?.getString("description")
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(navController)
        }
    }
}

