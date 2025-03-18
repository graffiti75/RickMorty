package com.cericatto.rickmortyreddit.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cericatto.rickmortyreddit.ui.list_screen.ListScreenRoot

@Composable
fun NavHostComposable(
	modifier: Modifier = Modifier
) {
	val navController = rememberNavController()
	NavHost(
		navController = navController,
		startDestination = Route.MainScreen
	) {
		composable<Route.MainScreen> {
			ListScreenRoot(
				modifier = modifier
			)
		}
	}
}