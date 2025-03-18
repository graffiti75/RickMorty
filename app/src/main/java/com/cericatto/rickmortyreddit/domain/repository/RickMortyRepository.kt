package com.cericatto.rickmortyreddit.domain.repository

import com.cericatto.rickmortyreddit.domain.errors.DataError
import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter
import com.cericatto.rickmortyreddit.domain.errors.Result

interface RickMortyRepository {

	suspend fun fetchCharacters(page: Int): Result<List<RickMortyCharacter>, DataError>
}