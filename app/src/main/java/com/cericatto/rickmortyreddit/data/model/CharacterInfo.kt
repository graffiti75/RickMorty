package com.cericatto.rickmortyreddit.data.model

data class CharacterInfo(
	val count : Int = 826,
	val pages: Int = 42,
	val next: String = "https://rickandmortyapi.com/api/character?page=2",
//	val prev: null
)