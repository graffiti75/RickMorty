package com.cericatto.rickmortyreddit.data.remote

import com.cericatto.rickmortyreddit.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickMortyApi {

	companion object {
		const val BASE_URL = "https://rickandmortyapi.com/api/"

	}

	/**
	 * Gets all characters.
	 */
	@GET("character")
	suspend fun fetchCharacters(
		@Query("page") page: Int
	): CharacterResponse
}