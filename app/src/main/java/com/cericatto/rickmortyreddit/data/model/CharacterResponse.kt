package com.cericatto.rickmortyreddit.data.model

data class CharacterResponse(
	val info: CharacterInfo,
	val results: List<RickMortyCharacter>,
)