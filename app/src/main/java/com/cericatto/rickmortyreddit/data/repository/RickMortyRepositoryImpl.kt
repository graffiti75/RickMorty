package com.cericatto.rickmortyreddit.data.repository

import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter
import com.cericatto.rickmortyreddit.data.remote.RickMortyApi
import com.cericatto.rickmortyreddit.domain.errors.DataError
import com.cericatto.rickmortyreddit.domain.errors.Result
import com.cericatto.rickmortyreddit.domain.errors.checkHttpException
import com.cericatto.rickmortyreddit.domain.repository.RickMortyRepository
import retrofit2.HttpException
import java.io.IOException

class RickMortyRepositoryImpl(
//	private val appScope: CoroutineScope,
//	private val dao: RickMortyDao,
	private val api: RickMortyApi,
) : RickMortyRepository {

	/*
	override suspend fun fetchCharacters(): Result<List<Character>, DataError> {
		return try {
			val list = api.fetchCharacters()
			Result.Success(data = list.results)
		} catch (e: HttpException) {
			checkHttpException(e.code())
		} catch (e: IOException) {
			// Likely no internet or server not reachable.
			Result.Error(DataError.Network.NO_INTERNET)
		}
	}
	 */

	override suspend fun fetchCharacters(page: Int): Result<List<RickMortyCharacter>, DataError> {
		return try {
			val response = api.fetchCharacters(page)
			Result.Success(data = response.results)
		} catch (e: HttpException) {
			checkHttpException(e.code())
		} catch (e: IOException) {
			Result.Error(DataError.Network.NO_INTERNET)
		}
	}
}