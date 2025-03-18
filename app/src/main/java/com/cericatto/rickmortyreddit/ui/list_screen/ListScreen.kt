package com.cericatto.rickmortyreddit.ui.list_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cericatto.rickmortyreddit.data.model.characterList
import com.cericatto.rickmortyreddit.ui.common.CharacterListItem
import com.cericatto.rickmortyreddit.ui.common.DynamicStatusBarColor
import com.cericatto.rickmortyreddit.ui.common.EmptyListContent
import com.cericatto.rickmortyreddit.ui.common.ErrorSnackbar
import com.cericatto.rickmortyreddit.ui.utils.backgroundColor
import com.cericatto.rickmortyreddit.ui.utils.contentColor
import com.cericatto.rickmortyreddit.ui.utils.isLandscapeOrientation

@Composable
fun ListScreenRoot(
	modifier: Modifier = Modifier,
	viewModel: ListScreenViewModel = hiltViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	DynamicStatusBarColor()
	ListScreen(
		modifier = modifier,
		onAction = viewModel::onAction,
		state = state
	)
}

@Composable
private fun ListScreen(
	modifier: Modifier = Modifier,
	onAction: (ListScreenAction) -> Unit,
	state: ListScreenState
) {
	if (state.loading) {
		Box(
			modifier = Modifier
				.padding(vertical = 20.dp)
				.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			CircularProgressIndicator(
				color = contentColor(),
				strokeWidth = 4.dp,
				modifier = Modifier.size(64.dp)
			)
		}
	} else {
		if (state.characters.isNotEmpty()) {
			MainScreenContent(
				modifier = modifier,
				onAction = onAction,
				state = state
			)
		} else {
			EmptyListContent(
				onAction = onAction
			)
		}
	}
}

@Composable
private fun MainScreenContent(
	modifier: Modifier = Modifier,
	onAction: (ListScreenAction) -> Unit,
	state: ListScreenState
) {
	val isLandscape = isLandscapeOrientation()
	// Separate states for list and grid, persisted with rememberSaveable.
	val listState = rememberSaveable(saver = LazyListState.Saver) {
		LazyListState()
	}
	val gridState = rememberSaveable(saver = LazyGridState.Saver) {
		LazyGridState()
	}

	// Sync scroll positions when orientation changes.
	LaunchedEffect(isLandscape) {
		if (isLandscape) {
			// From portrait to landscape: sync grid with list.
			val listFirstVisible = listState.firstVisibleItemIndex
			val listOffset = listState.firstVisibleItemScrollOffset
			gridState.scrollToItem(listFirstVisible, listOffset)
		} else {
			// From landscape to portrait: sync list with grid.
			val gridFirstVisible = gridState.firstVisibleItemIndex
			val gridOffset = gridState.firstVisibleItemScrollOffset
			listState.scrollToItem(gridFirstVisible, gridOffset)
		}
	}

	Box(
		contentAlignment = Alignment.BottomCenter,
		modifier = Modifier.fillMaxSize()
	) {
		if (isLandscape) {
			TwoColumnGrid(
				gridState = gridState,
				onAction = onAction,
				state = state
			)
		} else {
			OneColumnGrid(
				modifier = modifier,
				listState = listState,
				onAction = onAction,
				state = state
			)
		}
		if (state.performAnimation) {
			ErrorSnackbar(
				onAction = onAction,
				state = state
			)
		}
	}
}

@Composable
private fun OneColumnGrid(
	modifier: Modifier = Modifier,
	listState: LazyListState,
	onAction: (ListScreenAction) -> Unit,
	state: (ListScreenState)
) {
	LazyColumn(
		state = listState,
		modifier = modifier.fillMaxSize()
			.background(backgroundColor()),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		itemsIndexed(state.characters) { index, item ->
			// Trigger loading when 5 items from bottom are visible.
			if (index == state.characters.size - 5 &&
				!state.isLoadingMore && !state.hasReachedEnd
			) {
				onAction(ListScreenAction.LoadMore)
			}
			CharacterListItem(
				index = index,
				item = item,
				modifier = Modifier.wrapContentHeight()
			)
		}

		// Loading more indicator.
		if (state.isLoadingMore) {
			item {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.padding(16.dp),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(
						color = contentColor(),
						strokeWidth = 4.dp,
						modifier = Modifier.size(64.dp)
					)
				}
			}
		}
	}
}

@Composable
private fun TwoColumnGrid(
	gridState: LazyGridState,
	onAction: (ListScreenAction) -> Unit,
	state: (ListScreenState)
) {
	LazyVerticalGrid(
		columns = GridCells.Fixed(2),
		state = gridState,
		content = {
			itemsIndexed(state.characters) { index, item ->
				// Trigger loading when 5 items from bottom are visible.
				if (index == state.characters.size - 5 &&
					!state.isLoadingMore && !state.hasReachedEnd
				) {
					onAction(ListScreenAction.LoadMore)
				}
				CharacterListItem(
					index = index,
					item = item,
					modifier = Modifier.wrapContentHeight()
				)
			}

			// Loading more indicator.
			if (state.isLoadingMore) {
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(16.dp),
						contentAlignment = Alignment.Center
					) {
						CircularProgressIndicator(
							color = contentColor(),
							strokeWidth = 4.dp,
							modifier = Modifier.size(64.dp)
						)
					}
				}
			}
		}
	)
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
	ListScreen(
		onAction = {},
		state = ListScreenState().copy(
			loading = false,
			characters = characterList()
		)
	)
}

@Preview(showBackground = true)
@Composable
private fun MainContentContentPreview() {
	MainScreenContent(
		onAction = {},
		modifier = Modifier,
		state = ListScreenState()
			.copy(
				loading = false,
				characters = characterList()
			)
	)
}