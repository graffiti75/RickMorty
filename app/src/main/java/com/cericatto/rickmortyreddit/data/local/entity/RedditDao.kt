package com.cericatto.rickmortyreddit.data.local.entity

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RedditDao {

	@Upsert
	suspend fun upsertAllItems(items: List<RedditEntity>)

	// TODO Deal with Pagination later.
	@Query("SELECT * FROM reddit_table ORDER BY id DESC")
	suspend fun fetchAllItems(): List<RedditEntity>
}