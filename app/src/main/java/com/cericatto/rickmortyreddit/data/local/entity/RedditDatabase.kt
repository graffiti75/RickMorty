package com.cericatto.rickmortyreddit.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [
	RedditEntity::class
], version = 1, exportSchema = false)
abstract class RedditDatabase : RoomDatabase() {
	abstract val dao: RedditDao
}