package com.example.strmtv.presentation.home.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.strmtv.presentation.home.HomeScreen
import com.example.strmtv.presentation.home.SharedViewModel
import com.example.strmtv.presentation.home.detail.DetailScreen
import com.example.strmtv.presentation.home.search.SearchScreen

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail"
    const val SEARCH = "search"
}

@Composable
fun NavGraph(navController: NavHostController, sharedViewModel: SharedViewModel) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        // HomeScreen SIN sharedViewModel (no lo necesita)
        composable(Routes.HOME) {
            HomeScreen(navController)
        }

        // DetailScreen usando collectAsState
        composable(Routes.DETAIL) {
            val item by sharedViewModel.selectedItem.collectAsState()

            if (item != null) {
                DetailScreen(
                    item = item!!,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // SearchScreen con sharedViewModel (sí lo necesita para selectItem)
        composable(Routes.SEARCH) {
            SearchScreen(navController, sharedViewModel)
        }
    }
}