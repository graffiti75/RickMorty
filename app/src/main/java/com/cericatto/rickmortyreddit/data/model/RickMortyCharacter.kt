package com.cericatto.rickmortyreddit.data.model

data class RickMortyCharacter(
	val id : Int = 1,
	val name : String = "Rick Sanchez",
	val status : String = "Alive",
	val species: String = "Human",
	val type : String = "",
	val gender: String = "Male",
	val origin: Origin = Origin(),
	val location : Location = Location(),
	val image : String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
	val episode : Array<String> = arrayOf(
		"https://rickandmortyapi.com/api/episode/1",
		"https://rickandmortyapi.com/api/episode/2"
	),
	val url : String = "https://rickandmortyapi.com/api/character/1",
	val created : String = "2017-11-04T18:48:46.250Z"
)

fun characterList() = List(10) { RickMortyCharacter() }