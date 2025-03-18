package com.cericatto.rickmortyreddit.ui.list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cericatto.rickmortyreddit.domain.errors.DataError
import com.cericatto.rickmortyreddit.domain.errors.Result
import com.cericatto.rickmortyreddit.domain.repository.RickMortyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListScreenViewModel @Inject constructor(
	private val rickMortyRepository: RickMortyRepository
): ViewModel() {

	private val _state = MutableStateFlow(ListScreenState())
	val state: StateFlow<ListScreenState> = _state.asStateFlow()

	fun onAction(action: ListScreenAction) {
		when (action) {
			is ListScreenAction.OnRetry -> fetchData()
			is ListScreenAction.LoadMore -> fetchMoreCharacters()
			is ListScreenAction.ClearError -> clearError()
		}
	}

	init {
		fetchData()
	}

	private fun clearError() {
		_state.update { it.copy(errorMessage = null) }
	}

	private fun fetchData() {
		_state.update { it.copy(loading = false) }
		fetchCharacters(page = 1)
	}

	private fun fetchMoreCharacters() {
		if (!_state.value.isLoadingMore && !_state.value.hasReachedEnd) {
			_state.update { it.copy(isLoadingMore = true) }
			fetchCharacters(page = _state.value.currentPage + 1)
		}
	}

	private fun fetchCharacters(page: Int) {
		viewModelScope.launch {
			when (val result = rickMortyRepository.fetchCharacters(page = page)) {
				is Result.Error -> {
					_state.update { state ->
						state.copy(
							loading = false,
							isLoadingMore = false,
							isConnected = false,
							errorMessage = when (result.error) {
								DataError.Network.NO_INTERNET -> "No internet connection"
								else -> "Failed to load characters: ${result.error}"
							},
							performAnimation = true
						)
					}
				}
				is Result.Success -> {
					_state.update { it.copy(isConnected = true) }
					_state.update { state ->
						state.copy(
							loading = false,
							isLoadingMore = false,
							characters = if (page == 1) result.data else state.characters + result.data,
							currentPage = page,
							hasReachedEnd = result.data.isEmpty(), // If empty, we've reached the end.
							errorMessage = null,
							performAnimation = false
						)
					}
				}
			}
		}
	}
}