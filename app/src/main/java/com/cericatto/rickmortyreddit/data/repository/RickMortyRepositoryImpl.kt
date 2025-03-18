package com.cericatto.rickmortyreddit.data.repository

import com.cericatto.rickmortyreddit.data.local.entity.RedditDao
import com.cericatto.rickmortyreddit.data.mappers.toRedditEntity
import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter
import com.cericatto.rickmortyreddit.data.remote.RickMortyApi
import com.cericatto.rickmortyreddit.domain.errors.DataError
import com.cericatto.rickmortyreddit.domain.errors.Result
import com.cericatto.rickmortyreddit.domain.errors.checkHttpException
import com.cericatto.rickmortyreddit.domain.repository.RickMortyRepository
import retrofit2.HttpException
import java.io.IOException

class RickMortyRepositoryImpl(
	private val dao: RedditDao,
	private val api: RickMortyApi,
) : RickMortyRepository {

	// TODO Deal with pagination later.
	override suspend fun fetchCharacters(page: Int): Result<List<RickMortyCharacter>, DataError> {
		return try {
			val response = api.fetchCharacters(page)
			if (response.results.isNotEmpty()) {
				dao.upsertAllItems(
					items = response.results.map {
						it.toRedditEntity()
					}
				)
				Result.Success(data = response.results)
			} else {
				Result.Success(data = emptyList())
			}
		} catch (e: HttpException) {
			checkHttpException(e.code())
		} catch (e: IOException) {
			Result.Error(DataError.Network.NO_INTERNET)
		}
	}
}