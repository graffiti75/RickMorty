package com.cericatto.rickmortyreddit.ui.list_screen

import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter

data class ListScreenState(
	val loading : Boolean = true,
	var characters : List<RickMortyCharacter> = emptyList(),
	var isConnected : Boolean = true,
	val currentPage: Int = 1,
	val isLoadingMore: Boolean = false,
	val hasReachedEnd: Boolean = false,
	val errorMessage: String? = null,
	val performAnimation : Boolean = false
)