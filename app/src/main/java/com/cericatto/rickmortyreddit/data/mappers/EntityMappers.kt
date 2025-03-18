package com.cericatto.rickmortyreddit.data.mappers

import com.cericatto.rickmortyreddit.data.local.entity.RedditEntity
import com.cericatto.rickmortyreddit.data.model.RickMortyCharacter

fun RickMortyCharacter.toRedditEntity(): RedditEntity {
	return RedditEntity(
		id = this.id,
		name = this.name,
		status = this.status,
		species = this.species,
		type = this.type,
		gender = this.gender,
		image = this.image
	)
}