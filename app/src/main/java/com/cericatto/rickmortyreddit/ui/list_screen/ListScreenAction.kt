package com.cericatto.rickmortyreddit.ui.list_screen

sealed interface ListScreenAction {
	data object OnRetry : ListScreenAction
	data object LoadMore : ListScreenAction
	data object ClearError : ListScreenAction
}