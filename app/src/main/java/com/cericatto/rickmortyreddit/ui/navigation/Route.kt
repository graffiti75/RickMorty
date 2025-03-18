package com.cericatto.rickmortyreddit.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
	@Serializable
	data object MainScreen: Route
}