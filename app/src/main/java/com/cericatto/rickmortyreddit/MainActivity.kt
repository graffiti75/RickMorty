package com.cericatto.rickmortyreddit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cericatto.rickmortyreddit.ui.navigation.NavHostComposable
import com.cericatto.rickmortyreddit.ui.theme.RickMortyRedditTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			RickMortyRedditTheme {
				val systemBarsPadding = WindowInsets.systemBars.asPaddingValues()
				Surface(
					modifier = Modifier
						.fillMaxSize()
						.padding(systemBarsPadding) // Explicitly apply padding here
				) {
					NavHostComposable(
						modifier = Modifier.fillMaxSize() // Pass a clean modifier
					)
				}
			}
		}
	}
}