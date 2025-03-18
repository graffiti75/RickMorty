package com.cericatto.rickmortyreddit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reddit_table")
data class RedditEntity(
	@PrimaryKey val key: String = UUID.randomUUID().toString(),
	val id : Int,
	val name : String,
	val status : String,
	val species: String,
	val type : String,
	val gender: String,
	val image : String
)